# EmployeeWorkHours - Ứng Dụng Quản Lý Giờ Làm & Tính Lương Nhân Viên

**EmployeeWorkHours** là ứng dụng Android được thiết kế nhằm giúp doanh nghiệp tự động hóa và tối ưu hóa quá trình quản lý nhân sự, theo dõi thời gian làm việc (chấm công), tính giờ tăng ca (Overtime) và quản lý bảng lương của nhân viên một cách chính xác, minh bạch.

---

## 🚀 Tính Năng Chính

### 🔑 1. Đăng Nhập & Phân Quyền (Authentication & Authorization)
* **Phân quyền hệ thống:** Phân chia rõ ràng giữa 2 vai trò: **Admin (Quản trị viên)** và **Employee (Nhân viên)**.
* **Quản lý phiên đăng nhập:** Lưu trạng thái đăng nhập an toàn thông qua `SessionManager`.

### 👨‍💼 2. Phân Hệ Quản Trị (Admin)
* **Quản lý nhân sự (CRUD):** Thêm mới, chỉnh sửa thông tin, xóa hoặc tìm kiếm nhân viên theo tên/email.
* **Theo dõi & Tính lương:**
  * Lọc danh sách làm việc theo **Tháng/Năm**.
  * Tự động tính toán tổng số giờ làm chuẩn, giờ làm thêm (Overtime) và số tiền lương tương ứng.
  * Theo dõi trạng thái thanh toán lương (`PAID` / `UNPAID`).
* **Thống kê & Báo cáo:** Xem tổng quan bảng lương của toàn bộ nhân viên trong công ty.

### 👷 3. Phân Hệ Nhân Viên (Employee)
* **Xem bảng công cá nhân:** Theo dõi lịch sử thời gian vào/ra và tổng số giờ làm việc trong ngày/tháng.
* **Tra cứu thông tin lương:** Xem chi tiết bảng lương và trạng thái thanh toán của bản thân.

---

## 🛠️ Công Nghệ Sử Dụng

* **Ngôn ngữ lập trình:** Java
* **Nền tảng:** Android SDK
* **Cơ sở dữ liệu cục bộ:** Room Database (SQLite)
* **Kiến trúc ứng dụng:** Layered Architecture / MVC (Model - View - Controller / DAO Pattern)
* **Công cụ phát triển:** Android Studio

---

## 📂 Cấu Trúc Mã Nguồn (Project Structure)

```text
com.ptit.workhours/
│
├── data/                          # Lớp Cơ sở dữ liệu (Data Layer)
│   ├── Account.java               # Entity Tài khoản
│   ├── AccountDao.java            # DAO Thao tác với tài khoản
│   ├── AppDatabase.java           # Cấu hình Room Database chính
│   ├── Employee.java              # Entity Thông tin Nhân viên
│   ├── EmployeeDao.java           # DAO Thao tác với nhân viên
│   ├── Salary.java                # Entity Bảng lương
│   ├── SalaryDao.java             # DAO Thao tác với dữ liệu lương
│   ├── WorkLog.java               # Entity Chấm công / Giờ làm
│   └── WorkLogDao.java            # DAO Thao tác với nhật ký giờ làm
│
├── AdminMainActivity.java         # Màn hình chính dành cho Admin (Danh sách & Lọc lương)
├── AdminSalaryActivity.java       # Màn hình quản lý & thống kê lương
├── EmployeeEditActivity.java      # Màn hình thêm/sửa thông tin nhân viên
├── EmployeeMainActivity.java      # Màn hình chính dành cho Nhân viên
├── EmployeeSalaryActivity.java    # Màn hình xem lương cá nhân
├── LoginActivity.java             # Màn hình đăng nhập
├── AttendanceService.java         # Service xử lý chấm công
├── SalaryService.java             # Service xử lý logic tính lương
├── SessionManager.java            # Quản lý phiên làm việc người dùng
└── Utils.java                     # Các hàm tiện ích dùng chung
```

---

## 👥 Bảng Phân Công Công Việc Nhóm (3 Thành Viên)

### 👤 Thành viên 1: Cơ Sở Dữ Liệu & Logic Cốt Lõi (Database & Core Logic)
* **Vai trò:** Phụ trách toàn bộ lớp dữ liệu (Data Layer) và xử lý thuật toán nghiệp vụ.
* **Chi tiết công việc:**
  - [x] Thiết kế và xây dựng các lớp Entity: `Account`, `Employee`, `WorkLog`, `Salary`.
  - [x] Khởi tạo `AppDatabase` và triển khai các Interface DAO (`AccountDao`, `EmployeeDao`, `WorkLogDao`, `SalaryDao`).
  - [x] Xây dựng logic tự động tính toán tổng giờ làm, giờ tăng ca (Overtime) và số tiền lương trong `SalaryService` & `AttendanceService`.
  - [x] Đảm bảo tính toàn vẹn dữ liệu khi thao tác với Room Database.

### 👤 Thành viên 2: Phân Hệ Quản Trị (Admin Module)
* **Vai trò:** Xây dựng giao diện và xử lý logic cho các chức năng dành riêng cho Admin.
* **Chi tiết công việc:**
  - [x] Lập trình giao diện và xử lý sự kiện trên `AdminMainActivity` (Hiển thị danh sách, tìm kiếm nhân viên, nút bấm `CALCULATE` tính lương theo tháng/năm).
  - [x] Phát triển màn hình `AdminSalaryActivity` (Thống kê và xuất/quản lý trạng thái lương).
  - [x] Xây dựng màn hình `EmployeeEditActivity` (Thực hiện thêm, cập nhật, xóa nhân viên).
  - [x] Kết nối và hiển thị dữ liệu từ `EmployeeDao` và `SalaryDao` lên danh sách (`ListView`/`RecyclerView`).

### 👤 Thành viên 3: Đăng Nhập, Phân Hệ Nhân Viên & UI/UX (Auth, Employee & UI)
* **Vai trò:** Phụ trách luồng xác thực người dùng, phân quyền và giao diện dành cho Nhân viên.
* **Chi tiết công việc:**
  - [x] Phát triển màn hình `LoginActivity` và cơ chế kiểm soát phiên làm việc `SessionManager` (phân quyền Admin/Employee).
  - [x] Xây dựng màn hình `EmployeeMainActivity` và `EmployeeSalaryActivity` cho nhân viên xem lịch sử chấm công và bảng lương cá nhân.
  - [x] Thiết kế các file giao diện XML, chuẩn hóa màu sắc, font chữ và trải nghiệm người dùng (UX).
  - [x] Xây dựng các lớp tiện ích (`Utils`), kiểm thử ứng dụng (Testing) và viết tài liệu hướng dẫn (`README.md`).

---

## ⚙️ Hướng Dẫn Cài Đặt & Chạy Dự Án

1. **Clone repository về máy:**
   ```bash
   git clone https://github.com/nguyenthanhloq/EmployeeWorkHours.git
   ```
2. **Mở dự án:**
   * Mở **Android Studio**.
   * Chọn `Open an Existing Project` và trỏ tới thư mục vừa clone.
3. **Đồng bộ dự án:**
   * Chờ Gradle sync hoàn tất các thư viện cần thiết.
4. **Chạy ứng dụng:**
   * Chọn thiết bị ảo (Emulator) hoặc thiết bị thật (Android 8.0 trở lên).
   * Bấm nút **Run (Shift + F10)** để trải nghiệm ứng dụng.