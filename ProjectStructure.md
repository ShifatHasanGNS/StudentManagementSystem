.
├── cookies.txt
├── docker-compose.yml
├── Dockerfile
├── HELP.md
├── LICENSE
├── mvnw
├── mvnw.cmd
├── pom.xml
├── ProjectStructure.md
├── README.md
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── assignment
    │   │           └── studentmanagementsystem
    │   │               ├── controller
    │   │               │   ├── HomeController.java
    │   │               │   ├── ManagementController.java
    │   │               │   └── RegistrationController.java
    │   │               ├── DataLoader.java
    │   │               ├── model
    │   │               │   ├── Course.java
    │   │               │   ├── Department.java
    │   │               │   ├── Student.java
    │   │               │   └── Teacher.java
    │   │               ├── repository
    │   │               │   └── ManagementRepository.java
    │   │               ├── security
    │   │               │   ├── SecurityConfig.java
    │   │               │   └── UserAccount.java
    │   │               ├── service
    │   │               │   ├── ManagementService.java
    │   │               │   ├── UserDetailsServiceImpl.java
    │   │               │   └── UserService.java
    │   │               └── StudentManagementSystemApplication.java
    │   └── resources
    │       ├── application-dev.properties
    │       ├── application.properties
    │       ├── static
    │       │   └── css
    │       │       └── compact.css
    │       └── templates
    │           ├── access-denied.html
    │           ├── dashboard.html
    │           ├── entities
    │           │   ├── entity.html
    │           │   ├── list.html
    │           │   └── rows.html
    │           ├── fragments
    │           │   ├── entity-list.html
    │           │   ├── layout.html
    │           │   └── navbar.html
    │           ├── home.html
    │           ├── login.html
    │           ├── profile
    │           │   └── view.html
    │           └── register
    │               ├── choose-role.html
    │               ├── student.html
    │               └── teacher.html
    └── test
        ├── java
        │   └── com
        │       └── assignment
        │           └── studentmanagementsystem
        └── resources
