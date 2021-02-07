package com.awei.crm.service.impl;

import com.awei.crm.mapper.*;
import com.awei.crm.model.*;
import com.awei.crm.service.ClueService;
import com.awei.crm.utils.DateTimeUtil;
import com.awei.crm.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @program: CRM_bak
 * @author: Awei
 * @create: 2021-02-06 10:53
 **/
@Service("clueService")
public class ClueServiceImpl implements ClueService {

    @Autowired
    private ClueMapper clueMapper;

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ContactsMapper contactsMapper;

    @Autowired
    private ClueRemarkMapper clueRemarkMapper;
    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;

    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;
    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;
    @Autowired
    private TranMapper tranMapper;

    @Autowired
    private TranHistoryMapper tranHistoryMapper;

    @Override
    public int insert(Clue clue) {
        return clueMapper.insert(clue);
    }

    @Override
    public List<Map<String, Object>> pageList(Map<String, Object> map) {

        return clueMapper.pageList(map);
    }

    @Override

    public int pageListTotal() {
        return clueMapper.pageListTotal();
    }

    @Override
    public Clue getClueAndNameById(String id) {
        return clueMapper.getClueAndNameById(id);
    }

    @Override
    public int unBund(String id) {
        return clueMapper.unBund(id);
    }

    @Override
    public int deleteByPrimaryKey(String clueId) {
        return clueMapper.deleteByPrimaryKey(clueId);
    }

    @Override
    public boolean convert(Tran tran, User user, String clueId) {
        //(1) 获取到线索id，通过线索id获取线索对象（线索对象当中封装了线索的信息
        Clue c = clueMapper.selectByPrimaryKey(clueId);
        //(2) 通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
        Customer customer = customerMapper.selectByCompany(c.getCompany());
        if (null == customer) {
            customer = new Customer();
            customer.setWebsite(c.getWebsite());
            customer.setPhone(c.getPhone());
            customer.setOwner(c.getOwner());
            customer.setNextContactTime(c.getNextContactTime());
            customer.setName(c.getCompany());
            customer.setId(UUIDUtil.getUUID());
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setCreateBy(user.getId());
            customer.setDescription(c.getDescription());
            customer.setContactSummary(c.getContactSummary());
            customer.setAddress(c.getAddress());
            //新建 客户
            customerMapper.insert(customer);
        }
        //(3) 通过线索对象提取联系人信息，保存联系人
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setAddress(c.getAddress());
        contacts.setAppellation(c.getAppellation());
        contacts.setContactSummary(c.getContactSummary());
        contacts.setCreateTime(DateTimeUtil.getSysTime());
        contacts.setCreateBy(user.getId());
        contacts.setEmail(c.getEmail());
        contacts.setFullname(c.getFullname());
        contacts.setSource(c.getSource());
        contacts.setOwner(user.getId());
        contacts.setNextContactTime(contacts.getNextContactTime());
        contacts.setMphone(c.getMphone());
        contacts.setJob(c.getJob());
        contacts.setCustomerId(customer.getId());
        contactsMapper.insert(contacts);
        //(4) 线索备注转换到客户备注以及联系人备注
        ClueRemark clueRemark = clueRemarkMapper.selectByPrimaryKey(clueId);
        if (null != clueRemark) {
            CustomerRemark customerRemark = new CustomerRemark();
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            customerRemark.setId(UUIDUtil.getUUID());

            contactsRemark.setNoteContent(clueRemark.getNoteContent());
            customerRemark.setNoteContent(clueRemark.getNoteContent());

            contactsRemark.setContactsId(contacts.getId());
            customerRemark.setCustomerId(customer.getId());

            contactsRemark.setCreateBy(user.getId());
            customerRemark.setCreateBy(user.getId());
            contactsRemark.setCreateTime(DateTimeUtil.getSysTime());
            customerRemark.setCreateTime(DateTimeUtil.getSysTime());

            contactsRemark.setEditFlag("0");
            customerRemark.setEditFlag("0");
            contactsMapper.insert(contacts);
            customerRemarkMapper.insert(customerRemark);
        }


        //(5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系
        List<ClueActivityRelation> relationList = clueActivityRelationMapper.selectByClueId(clueId);
        if (null != relationList) {
            for (ClueActivityRelation re : relationList) {
                ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
                contactsActivityRelation.setActivityId(re.getActivityId());
                contactsActivityRelation.setContactsId(re.getClueId());
                contactsActivityRelation.setId(UUIDUtil.getUUID());
                contactsActivityRelationMapper.insert(contactsActivityRelation);
            }
            //把线索和市场活动 的关系 干掉
            clueActivityRelationMapper.deleteByClueId(clueId);
        }


        //(6) 如果有创建交易需求，创建一条交易
        if (null != tran) {
            String tranId = UUIDUtil.getUUID();
            tran.setId(tranId);
            tran.setCreateTime(DateTimeUtil.getSysTime());
            tran.setCreateBy(user.getId());
            tran.setContactsId(contacts.getId());
            tran.setCustomerId(customer.getId());
            tranMapper.insert(tran);
            //(7) 如果创建了交易，则创建一条该交易下的交易历史
            TranHistory history = new TranHistory();
            history.setId(UUIDUtil.getUUID());
            history.setCreateBy(tran.getCreateBy());
            history.setCreateTime(DateTimeUtil.getSysTime());
            history.setTranId(tranId);
            history.setMoney(tran.getMoney());
            history.setStage(tran.getStage());
            history.setExpectedDate(tran.getExpectedDate());
            tranHistoryMapper.insert(history);
        }
        //(8) 删除线索备注
        int clueDeleteNum = clueRemarkMapper.deleteByClueId(clueId);
        //(9) 删除线索和市场活动的关系
        int clueActRelationNum = clueActivityRelationMapper.deleteByClueId(clueId);
        //(10) 删除线索
        int deleteClueNum = clueMapper.deleteByPrimaryKey(clueId);


        return true;
    }


}
