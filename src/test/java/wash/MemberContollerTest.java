package wash;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.bridge4biz.wash.data.UserData;
import com.bridge4biz.wash.mybatis.MybatisDAO;
import com.bridge4biz.wash.mybatis.MybatisMapper;
import com.bridge4biz.wash.service.AuthUser;
import com.bridge4biz.wash.service.Coupon;
import com.bridge4biz.wash.service.Item;
import com.bridge4biz.wash.service.ItemCode;
import com.bridge4biz.wash.service.Member;
import com.bridge4biz.wash.service.Order;
import com.bridge4biz.wash.util.Constant;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="file:src/main/webapp/WEB-INF/spring/mybatis-config.xml")
@TransactionConfiguration(defaultRollback=true)
public class MemberContollerTest {
	@Autowired
	MybatisDAO mybatisDAO;
	
	@Autowired
	MybatisMapper mapper;
	
	private Order order;
	private UserData user;
	private int uid;
	
	/*
	 * 테스트 전 주문 하나를 생성
	 */
	@Before
	@Transactional
	public void setUp() {		
		// User Setting
		user = new UserData();
		user.email = "test1115@test.com";
		user.phone = "01000000000";

		int constant = mybatisDAO.registerUserForMember(user);
		assertEquals(Constant.SUCCESS, constant);
		
		Member member = mapper.getMember(user.email);
		
		this.uid = member.uid;
		
		// Order Setting
		order = new Order();
		order.address = "서울특별시 도봉구 역삼동";
		order.addr_building = "대암빌딩 104호";
		order.price = 12000;
		order.dropoff_price = 2000;
		order.coupon = new ArrayList<Coupon>();
		order.pickup_date = "2015-01-01 10:00:00";
		order.dropoff_date = "2015-02-01 10:00:00";
		
		ArrayList<Item> items = new ArrayList<Item>();
		ArrayList<ItemCode> itemCode = mapper.getItemCode();
		
		Item shirts = new Item(itemCode.get(0));
		shirts.count = 5;
		items.add(shirts);
		order.item = items;
	}
	
	/*
	 * 가입 테스트
	 */
	@Test
	@Transactional
	public void testMemberNewJoin() {
		UserData user2 = new UserData();
		user2.email = "test1115@test.com";
		user2.phone = "01000000000";
		
		int constant = mybatisDAO.registerUserForMember(user2);
		assertEquals(Constant.ACCOUNT_DUPLICATION, constant);
		
		Member member = mapper.getMember(user.email);	
		assertEquals(user.email, member.email);
	}
	
	@Test
	@Transactional
	public void testCheckPrice() {
		boolean success = mybatisDAO.priceCheck(order, uid);
		assertTrue(success);
	}
	
	@Test
	@Transactional
	public void testAddNewOrder() {
		int constant = mybatisDAO.addNewOrder(order, uid);
		assertEquals(Constant.SUCCESS, constant);
	}
	
	@Test
	@Transactional
	public void testModifyMemberOrderItem() {
		int constant = mybatisDAO.addNewOrder(order, uid);
		assertEquals(Constant.SUCCESS, constant);
		
		ArrayList<Order> orders = mapper.getOrder(uid);
		Order insertedOrder = orders.get(0);
		
		insertedOrder.price = 30000;
		insertedOrder.dropoff_price = 0;
		insertedOrder.item = mapper.getItem(insertedOrder.oid);

		constant = mybatisDAO.modifyOrderItem(insertedOrder, uid);
		assertEquals(Constant.SUCCESS, constant);

		Order modifiedOrder = mybatisDAO.getOrder(uid).get(0);
		assertFalse(order.price == modifiedOrder.price);
	}
	
	@Test
	@Transactional
	public void testAddAuthUser() {
		boolean success = mybatisDAO.getAuthorizationCode(uid, this.user.phone);
		assertTrue(success);
		
		AuthUser authUser = new AuthUser();
		authUser.uid = this.user.uid;
		authUser.email = this.user.email;
		authUser.phone = this.user.phone;
		authUser.code = mapper.getAuthorizationCode(uid);
		
		int constant = mybatisDAO.addAuthUser(authUser, uid);
		assertEquals(Constant.SUCCESS, constant);
	}
	
//	@Test
//	@Transactional
//	public void testAddAuthUserWithRegister() {
//		UserData user3 = new UserData();
//		user3.email = "test1117@test.com";
//		user3.phone = "01000000000";
//		
//		int constant = mybatisDAO.registerUserForMember(user3);
//		assertEquals(Constant.SUCCESS, constant);
//		
//		Member member = mapper.getMember(user.email);
//		int count = mapper.isAuthUser(member.uid);
//		assertTrue(count > 0);
//	}
}
