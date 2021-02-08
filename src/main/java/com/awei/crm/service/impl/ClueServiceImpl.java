package com.awei.crm.service.impl;

import com.awei.crm.exception.CULDException;
import com.awei.crm.mapper.*;
import com.awei.crm.model.*;
import com.awei.crm.service.ClueService;
import com.awei.crm.utils.DateTimeUtil;
import com.awei.crm.utils.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(

            propagation = Propagation.REQUIRED,
            rollbackFor = {NullPointerException.class,CULDException.class},
            isolation = Isolation.DEFAULT
    )
    @Override
    public boolean convert(Tran tran, User user, String clueId) throws CULDException {

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
           int insertNum = customerMapper.insert(customer);
            if (insertNum ==0) {
                //插入失败
                throw new CULDException("添加客户信息失败,客户ID：" + customer.getId());
            }
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
        int insertNum = contactsMapper.insert(contacts);
        if (insertNum == 0) {
            throw new CULDException("添加联系人信息失败，联系人ID：" + contacts.getId());
        }
        //(4) 线索备注转换到客户备注以及联系人备注
        List<ClueRemark> clRmkList = clueRemarkMapper.selectByClueId(clueId);
        if (null != clRmkList) {
            for (ClueRemark clueRemark : clRmkList) {
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
                insertNum= customerRemarkMapper.insert(customerRemark);
                if (insertNum == 0) {
                    throw new CULDException("添加客户备注失败,客户ID：" + customerRemark.getCustomerId());
                }
                insertNum=contactsRemarkMapper.insert(contactsRemark);
                if (insertNum == 0) {
                    throw new CULDException("添加联系人备注失败联系人ID：" + contactsRemark.getContactsId());
                }
            }
        }


        //(5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系
        List<ClueActivityRelation> relationList = clueActivityRelationMapper.selectByClueId(clueId);
        if (null != relationList) {
            for (ClueActivityRelation re : relationList) {
                ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
                contactsActivityRelation.setActivityId(re.getActivityId());
                contactsActivityRelation.setContactsId(re.getClueId());
                contactsActivityRelation.setId(UUIDUtil.getUUID());
                insertNum = contactsActivityRelationMapper.insert(contactsActivityRelation);
                if (insertNum == 0) {
                    throw new CULDException("添加联系人与市场活动的关系失败,关系ID： " + contactsActivityRelation.getId());
                }

            }
        }


        //(6) 如果有创建交易需求，创建一条交易
        if (null != tran) {
            String tranId = UUIDUtil.getUUID();
            tran.setId(tranId);
            tran.setCreateTime(DateTimeUtil.getSysTime());
            tran.setCreateBy(user.getId());
            tran.setContactsId(contacts.getId());
            tran.setCustomerId(customer.getId());
            insertNum = tranMapper.insert(tran);
            if (insertNum == 0) {
                throw new CULDException("添加交易失败,交易客户ID：" + tran.getCustomerId());
            }
            //(7) 如果创建了交易，则创建一条该交易下的交易历史
            TranHistory history = new TranHistory();
            history.setId(UUIDUtil.getUUID());
            history.setCreateBy(tran.getCreateBy());
            history.setCreateTime(DateTimeUtil.getSysTime());
            history.setTranId(tranId);
            history.setMoney(tran.getMoney());
            history.setStage(tran.getStage());
            history.setExpectedDate(tran.getExpectedDate());
            insertNum = tranHistoryMapper.insert(history);
            if (insertNum == 0) {
                throw new CULDException("添加交易历史记录失败,交易ID：" + tran.getId());
            }
        }
        //(8) 删除线索备注
        List<ClueRemark> crList = clueRemarkMapper.selectByClueId(clueId);
        if (crList.size()!=0) {
            //不为空，说明该线索有备注，把他干掉
            int deleteNum = clueRemarkMapper.deleteByClueId(clueId);
            if (deleteNum == 0) {
                throw new CULDException("删除该线索的备注失败,线索ID："+clueId);
            }
        }
        //(9) 删除线索和市场活动的关系
        List<ClueActivityRelation> carList = clueActivityRelationMapper.selectByClueId(clueId);
        if (carList.size()!=0) {
            int deleteNum = clueActivityRelationMapper.deleteByClueId(clueId);
            if (deleteNum == 0) {
                throw new CULDException("删除线索跟市场活动的关系失败，线索ID：" + clueId);
            }
        }
        //(10) 删除线索
        int deleteClueNum = clueMapper.deleteByPrimaryKey(clueId);
        if (deleteClueNum == 0) {
            throw new CULDException("删除线索失败，线索ID:" + clueId);
        }
        return true;
    }


}
