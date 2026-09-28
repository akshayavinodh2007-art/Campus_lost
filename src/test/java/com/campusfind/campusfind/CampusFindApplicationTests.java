package com.campusfind.campusfind;

import com.campusfind.campusfind.dto.CreateFoundItemRequest;
import com.campusfind.campusfind.dto.CreateLostReportRequest;
import com.campusfind.campusfind.dto.LoginRequest;
import com.campusfind.campusfind.dto.RegisterRequest;
import com.campusfind.campusfind.dto.UpdateStatusRequest;
import com.campusfind.campusfind.entity.Category;
import com.campusfind.campusfind.entity.ItemStatus;
import com.campusfind.campusfind.entity.Role;
import com.campusfind.campusfind.entity.User;
import com.campusfind.campusfind.service.AdminService;
import com.campusfind.campusfind.service.CategoryService;
import com.campusfind.campusfind.service.FoundItemService;
import com.campusfind.campusfind.service.LostReportService;
import com.campusfind.campusfind.service.MatchingService;
import com.campusfind.campusfind.service.UserService;
import com.campusfind.campusfind.exception.BusinessRuleViolationException;
import com.campusfind.campusfind.exception.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CampusFindApplicationTests {

	@Autowired
	private UserService userService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private LostReportService lostReportService;

	@Autowired
	private FoundItemService foundItemService;

	@Autowired
	private MatchingService matchingService;

	@Autowired
	private AdminService adminService;

	@Autowired
	private com.campusfind.campusfind.repository.FoundItemRepository foundItemRepository;

	@Autowired
	private com.campusfind.campusfind.repository.LostReportRepository lostReportRepository;

	@Autowired
	private com.campusfind.campusfind.repository.CategoryRepository categoryRepository;

	@Autowired
	private com.campusfind.campusfind.repository.UserRepository userRepository;

	private User student;
	private User staff;
	private User admin;
	private Category electronics;

	@BeforeEach
	void setUp() {
		foundItemRepository.deleteAll();
		lostReportRepository.deleteAll();
		categoryRepository.deleteAll();
		userRepository.deleteAll();

		student = userService.registerUser(new RegisterRequest("john_student", "pass123", Role.STUDENT));
		staff = userService.registerUser(new RegisterRequest("sarah_staff", "pass123", Role.STAFF));
		admin = userService.registerUser(new RegisterRequest("admin_user", "pass123", Role.ADMIN));
		electronics = categoryService.createCategory(new Category("Electronics"));
	}

	@Test
	void testUserRegistrationAndLogin() {
		User loggedIn = userService.loginUser(new LoginRequest("john_student", "pass123"));
		assertNotNull(loggedIn);
		assertEquals("john_student", loggedIn.getUsername());
		assertEquals(Role.STUDENT, loggedIn.getRole());
	}

	@Test
	void testBusinessRule1_AvailableToClaimed_ShouldWork() {
		var foundItem = foundItemService.createFoundItem(new CreateFoundItemRequest(
				"Black Dell Laptop with sticker", "Library 2nd Floor", LocalDate.now(), electronics.getId(), staff.getId()
		));
		assertEquals(ItemStatus.AVAILABLE, foundItem.getStatus());

		var updated = foundItemService.updateStatus(foundItem.getId(), new UpdateStatusRequest(staff.getId(), ItemStatus.CLAIMED));
		assertEquals(ItemStatus.CLAIMED, updated.getStatus());
	}

	@Test
	void testBusinessRule1_ClaimedToReturned_ShouldWork() {
		var foundItem = foundItemService.createFoundItem(new CreateFoundItemRequest(
				"Black Dell Laptop with sticker", "Library 2nd Floor", LocalDate.now(), electronics.getId(), staff.getId()
		));

		// AVAILABLE -> CLAIMED
		foundItemService.updateStatus(foundItem.getId(), new UpdateStatusRequest(staff.getId(), ItemStatus.CLAIMED));

		// CLAIMED -> RETURNED
		var returnedItem = foundItemService.updateStatus(foundItem.getId(), new UpdateStatusRequest(staff.getId(), ItemStatus.RETURNED));
		assertEquals(ItemStatus.RETURNED, returnedItem.getStatus());
	}

	@Test
	void testBusinessRule1_AvailableToReturned_MustFail() {
		var foundItem = foundItemService.createFoundItem(new CreateFoundItemRequest(
				"Black Dell Laptop with sticker", "Library 2nd Floor", LocalDate.now(), electronics.getId(), staff.getId()
		));

		// AVAILABLE -> RETURNED must fail
		assertThrows(BusinessRuleViolationException.class, () -> {
			foundItemService.updateStatus(foundItem.getId(), new UpdateStatusRequest(staff.getId(), ItemStatus.RETURNED));
		});
	}

	@Test
	void testBusinessRule2_UnauthorizedUser_MustFail() {
		var foundItem = foundItemService.createFoundItem(new CreateFoundItemRequest(
				"Black Dell Laptop with sticker", "Library 2nd Floor", LocalDate.now(), electronics.getId(), staff.getId()
		));

		// Student trying to change status must fail with UnauthorizedException
		assertThrows(UnauthorizedException.class, () -> {
			foundItemService.updateStatus(foundItem.getId(), new UpdateStatusRequest(student.getId(), ItemStatus.CLAIMED));
		});
	}

	@Test
	void testBusinessRule2_AdminUser_CanChangeStatus() {
		var foundItem = foundItemService.createFoundItem(new CreateFoundItemRequest(
				"Black Dell Laptop with sticker", "Library 2nd Floor", LocalDate.now(), electronics.getId(), staff.getId()
		));

		// Admin can change status
		var updated = foundItemService.updateStatus(foundItem.getId(), new UpdateStatusRequest(admin.getId(), ItemStatus.CLAIMED));
		assertEquals(ItemStatus.CLAIMED, updated.getStatus());
	}

	@Test
	void testMatchingFeature() {
		var foundItem = foundItemService.createFoundItem(new CreateFoundItemRequest(
				"Black Dell Laptop with Python sticker", "Library 2nd Floor", LocalDate.now(), electronics.getId(), staff.getId()
		));

		var lostReport = lostReportService.createLostReport(new CreateLostReportRequest(
				"Lost Dell Laptop with stickers", "Library 2nd Floor", LocalDate.now(), electronics.getId(), student.getId()
		));

		var matches = matchingService.findMatchesForLostReport(lostReport.getId());
		assertFalse(matches.isEmpty());
		assertEquals(foundItem.getId(), matches.get(0).getId());
	}

	@Test
	void testAdminDashboardData() {
		lostReportService.createLostReport(new CreateLostReportRequest(
				"Lost Dell Laptop", "Library", LocalDate.now(), electronics.getId(), student.getId()
		));

		var foundItem = foundItemService.createFoundItem(new CreateFoundItemRequest(
				"Found Dell Laptop", "Library", LocalDate.now(), electronics.getId(), staff.getId()
		));

		foundItemService.updateStatus(foundItem.getId(), new UpdateStatusRequest(staff.getId(), ItemStatus.CLAIMED));
		foundItemService.updateStatus(foundItem.getId(), new UpdateStatusRequest(staff.getId(), ItemStatus.RETURNED));

		var dashboard = adminService.getDashboardData();
		assertEquals(1, dashboard.getPendingLostReports());
		assertEquals(0, dashboard.getMatchedItems());
		assertEquals(1, dashboard.getReturnedItems());
	}
}
