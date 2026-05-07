# Cargo Register Overhaul Plan

## 0) Note from the vibe coder
- This whole thing is just for assignment for "Database and security" class
- Do not focus too much on web design UI/UX
- Create definitive, easy to understand foundation backend
- Main must have things are: user input sanitization,validation, prepared statements,
- replace or work on top of current files of index.php, admin.php, db.sql

## 1) Scope & Goals
- Build a very simple cargo registry for packages that have already arrived in Mongolia.
- Use only PHP, HTML, CSS, and MySQL.
- Keep it close to the current `index.php` and `admin.php` style, but cleaner.
- Use plain PHP sessions only.
- Keep the system focused on package registration, package lookup, and a simple customer dashboard.

## 2) Main Business Flow
1. Package arrives in Mongolia.
2. Admin or employee registers the package in the web interface.
3. The package is saved using only track code, 8-digit phone number, shelf, price, and created time.
4. Customer signs up with username, password, phone number, and email.
5. Customer logs in with username and password.
6. Customer dashboard shows only that customer’s own packages.
7. Admin or employee can mark the package as picked up.
8. No direct database editing is needed for normal work.

## 3) Target Features
### Public
- Search by track code.
- Search by 8-digit phone number.
- View package information.

### Customer
- Signup.
- Login and logout.
- View own package dashboard only.
- No profile editing, no extra actions.

### Staff
- Login and logout.
- Add package.
- Search packages.
- Mark package as picked up.
- Edit package if needed.

### Admin vs Employee
- Both admin and employee use the same `admin.php` file after login.
- The difference is only the role and available sections inside the dashboard.
- Employee: package registration, package search, package edit, pickup action.
- Admin: all employee actions plus user management and any extra control sections.

## 4) Database Design
### customers
- customerID (PK)
- fullName
- username (unique)
- email
- phoneNumber
- passwordHash
- createdAt

### employees
- employeeID (PK)
- fullName
- username (unique)
- phoneNumber
- passwordHash
- role (employee|admin)
- createdAt

### packages
- packageID (PK)
- trackCode
- phoneNumber
- shelf
- price
- isDeleted
- createdBy (employee username)
- createdAt

### Notes
- Keep package lookup only by track code and 8-digit phone number.
- Keep the package table small and simple.
- Customer packages are linked only by `phoneNumber`.
- Customers and employees are stored in separate tables.
- Customer login uses the `customers` table.
- Staff login uses the `employees` table.
- After login, `admin.php` checks the employee role:
  - `employee` sees package-only features.
  - `admin` sees package features plus user management.

## 5) Database Users & Permissions
### db_admin
- Full access for schema setup and maintenance.

### db_web
- Read-only access for public package search. (only able to search trackCode in index.php to get whether it is arrived or not)

### db_customer_app
- Can read packages for the customer dashboard.
- Can read and authenticate customer records from the `customers` table.

### db_employee
- Can insert, update, and read packages.
- Can read employee records from the `employees` table for staff login.

## 6) File Structure
- index.php: public package search
- signup.php: customer signup
- login.php: customer login
- dashboard.php: customer dashboard showing only own packages
- admin.php: package registration and pickup dashboard
- includes/db.php: database connection
- includes/auth.php: customer and staff login checks
- includes/validation.php: basic validation helpers
- includes/package.php: package helpers
- includes/customer.php: customer helpers
- includes/employee.php: employee helpers
- assets/style.css: basic styling

## 7) Form Handling Style
- Follow the same beginner style as `lab12.php`.
- Use one `clean_input()` helper.
- Use one error variable per field.
- Validate fields with simple `if (empty(...))` checks.
- Validate phone number as exactly 8 digits.
- Signup fields: phone number, email, username, password.
- Login fields: username, password.
- Show errors under each field.
- Keep posted values in the form.
- Show success only after all checks pass.
- Use prepared statements for every DB write or search.

## 8) SQL Injection Prevention
- Never build SQL with raw `$_POST` or `$_GET` values.
- Use prepared statements only.
- Bind search values like `%term%` in PHP.
- Cast IDs with `intval()`.
- Whitelist action names like login, add_package, update_package, mark_picked.
- Do not keep any open SQL command box on `admin.php`.
- Escape all output with `htmlspecialchars()`.

## 9) Page Fields and Logic
### `index.php`
- Fields: track code or phone number.
- Purpose: public package lookup.
- Output: track code, phone number, shelf, price, and picked-up state.

### `signup.php`
- Fields: phone number, email, username, password.
- Purpose: create customer account.

### `login.php`
- Fields: username, password.
- Purpose: authenticate customer or staff and start session.

### `dashboard.php`
- No edit fields.
- Shows only packages that match the logged-in customer phone number.
- Purpose: simple customer dashboard with own packages only.

### `admin.php`
- Package fields: track code, phone number, shelf, price.
- Staff fields: full name, phone number, role, password.
- Purpose: register packages and mark them picked up through the browser only.
- `admin.php` is the shared staff page for both employees and admins.
- `employee` role: can use package sections only.
- `admin` role: can use package sections and user management sections.

### `admin.php` role-based sections
- Employee sees:
  - add package form
  - search package form
  - package list table
  - mark picked up action
- Admin sees all employee sections plus:
  - customer management section, if needed later
  - employee management section, if needed later
  - extra reports, if needed later

## 10) `admin.php` Dashboard Plan
### Main sections
- Login form.
- Customer signup form.
- Customer dashboard view.
- Add package form.
- Search package form.
- Package list table.
- Mark picked up action.
- Role-based user management sections that appear only for admin.

### Main functions
- `clean_input()`
  - trims and sanitizes form data.
- `validate_phone()`
  - checks that phone number is exactly 8 digits.
- `validate_track_code()`
  - checks that the track code is not empty and safe.
- `add_package()`
  - inserts a package with track code, phone number, shelf, and price.
- `update_package()`
  - updates package info if needed.
- `mark_picked()`
  - marks a package as picked up using `isDeleted = 1` or a similar simple flag.
- `list_packages()`
  - loads package rows for the dashboard.
- `list_customer_packages()`
  - loads only the packages that match the logged-in customer's phone number.
- `add_customer()`
  - inserts a new customer into the `customers` table.
- `find_customer_by_username()`
  - finds a customer for login.
- `find_employee_by_username()`
  - finds a staff user for login.

### Role handling in `admin.php`
- Login succeeds by checking the `employees` table.
- If role is `employee`, show only package management sections.
- If role is `admin`, show package management sections and staff-only management sections.
- Keep the role check simple with an `if ($role === 'admin')` style condition.

### Package lifecycle in the web app
1. Package arrives in Mongolia.
2. Admin or employee registers it in `admin.php`.
3. Customer signs up and logs in.
4. Customer opens dashboard and sees only own packages.
5. The package is shown until picked up.
6. Admin or employee marks it as picked up.

## 11) Session Handling
- Use plain PHP sessions with `session_start()`.
- Very simple as possible, do not care too much about security for session

## 12) Integration Between Pages
- `db.php` provides the MySQL connection.
- `validation.php` provides shared checks for all forms.
- `auth.php` checks customer and staff login state and role.
- `package.php` handles all package queries.
- `customer.php` handles customer signup and login queries.
- `employee.php` handles staff login queries.
- `index.php` only reads package data for public lookup.
- `dashboard.php` reads only the logged-in customer's packages.
- `admin.php` handles package registration and pickup actions for both employee and admin.
- `admin.php` shows extra admin-only sections only when the logged-in role is `admin`.
- All pages use the same helper functions and the same simple form style.

## 13) Security
- Hash passwords with `password_hash()`.
- Verify passwords with `password_verify()`.
- Use prepared statements everywhere.
- Escape all displayed output.
- Use basic CSRF tokens for POST forms.
- Whitelist actions before running any DB query.

## 14) Data Migration
- Keep the first migration simple.
- Add one admin user and two employee users.
- Add sample customer users with username, phone, email, and password.
- Add sample packages using only track code, phone number, shelf, and price.
- Make sure all package work happens in the web interface.

## 15) Implementation Phases
1. Create schema and DB users.
2. Build staff login and session flow.
3. Build public package search.
4. Build admin package registration.
5. Build pickup marking flow.
6. Add validation and security hardening.
7. Test the package lifecycle end to end.

## 16) Deliverables
- Updated SQL schema.
- Simplified PHP pages.
- Shared helper files.
- CSS file.
- Short setup notes.
