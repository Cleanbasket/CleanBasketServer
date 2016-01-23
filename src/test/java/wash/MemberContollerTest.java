package wash;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;

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
import com.bridge4biz.wash.service.Address;
import com.bridge4biz.wash.service.AuthUser;
import com.bridge4biz.wash.service.Coupon;
import com.bridge4biz.wash.service.Item;
import com.bridge4biz.wash.service.ItemCode;
import com.bridge4biz.wash.service.Member;
import com.bridge4biz.wash.service.Order;
import com.bridge4biz.wash.service.PickupTime;
import com.bridge4biz.wash.util.Constant;
import com.bridge4biz.wash.util.TimeCheck;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/spring/mybatis-config.xml")
@TransactionConfiguration(defaultRollback = true)
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
		user.email = "test1116@test.com";
		user.phone = "01000000000";

		int constant = mybatisDAO.registerUserForMember(user);
		assertEquals(Constant.SUCCESS, constant);

		Member member = mapper.getMember(user.email);

		this.uid = member.uid;

		// Order Setting
		Calendar c = Calendar.getInstance();

		order = new Order();
		order.address = "서울특별시 강남구 역삼동";
		order.addr_building = "대암빌딩 104호";
		order.price = 12000;
		order.dropoff_price = 2000;
		order.coupon = new ArrayList<Coupon>();

		c.add(Calendar.HOUR_OF_DAY, 1);
		c.add(Calendar.MINUTE, 45);
		order.pickup_date = TimeCheck.getStringDateTime(c.getTime());

		c.add(Calendar.DAY_OF_MONTH, 2);
		order.dropoff_date = TimeCheck.getStringDateTime(c.getTime());

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
		user2.email = "test1116@test.com";
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

		Calendar c = Calendar.getInstance();
		c.setTime(TimeCheck.getDate(order.pickup_date));

		if (c.get(Calendar.MINUTE) < 45) {
			c.add(Calendar.HOUR_OF_DAY, -2);
			c.add(Calendar.MINUTE, +15);
		} else {
			c.add(Calendar.HOUR_OF_DAY, -1);
			c.add(Calendar.MINUTE, -45);
		}

		order.pickup_date = TimeCheck.getStringDateTime(c.getTime());
		constant = mybatisDAO.addNewOrder(order, uid);
		assertEquals(Constant.TOO_EARLY_TIME, constant);

		c.set(Calendar.HOUR_OF_DAY, 24);
		c.set(Calendar.MINUTE, 0);

		order.pickup_date = TimeCheck.getStringDateTime(c.getTime());
		constant = mybatisDAO.addNewOrder(order, uid);
		assertEquals(Constant.TOO_LATE_TIME, constant);

		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 29);

		order.pickup_date = TimeCheck.getStringDateTime(c.getTime());
		constant = mybatisDAO.addNewOrder(order, uid);
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

	@Test
	@Transactional
	public void testPickupDatetime() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.HOUR_OF_DAY, 2);

		mapper.addPickupTime(TimeCheck.getStringDateTime(c.getTime()), 0);

		ArrayList<PickupTime> pickupTimes = mapper.getPickupTime();

		assertEquals(TimeCheck.getStringDateTime(c.getTime()).substring(0, 18),
				pickupTimes.get(0).datetime.substring(0, 18));
	}

	@Test
	@Transactional
	public void testDropoffInterval() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, 1);

		mapper.addDropoffTime(TimeCheck.getStringDateTime(c.getTime()));

		Calendar now = Calendar.getInstance();

		assertEquals("3", mybatisDAO.getDropoffInterval(TimeCheck
				.getStringDateTime(now.getTime())));
	}

	@Test
	@Transactional
	public void testUpdateAddress() {
		Address oldAddress = new Address(uid, "서울특별시 강남구 역삼동", "605-12");
		Address newAddress = new Address(uid, "서울특별시 강남구 자곡동", "크린바스켓");
		
		if (mapper.getNumberOfAddressByUid(uid) == 0) {
			Boolean success = mybatisDAO.updateMemberAddress(oldAddress, uid);
			assertTrue(success);
		}
		
		mybatisDAO.updateMemberAddress(newAddress, uid);
		
		int adrid = mapper.getAddressByUid(uid);
		Address getAddress = mapper.getAddressForSingle(adrid, uid);
		
		assertEquals(getAddress.address, newAddress.address);
	}
	
	// @Test
	// @Transactional
	// public void testAddAuthUserWithRegister() {
	// UserData user3 = new UserData();
	// user3.email = "test1117@test.com";
	// user3.phone = "01000000000";
	//
	// int constant = mybatisDAO.registerUserForMember(user3);
	// assertEquals(Constant.SUCCESS, constant);
	//
	// Member member = mapper.getMember(user.email);
	// int count = mapper.isAuthUser(member.uid);
	// assertTrue(count > 0);
	// }
}
