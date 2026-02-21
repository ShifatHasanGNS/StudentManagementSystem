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
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── assignment
│   │   │           └── studentmanagementsystem
│   │   │               ├── controller
│   │   │               │   ├── HomeController.java
│   │   │               │   ├── ManagementController.java
│   │   │               │   └── RegistrationController.java
│   │   │               ├── DataLoader.java
│   │   │               ├── model
│   │   │               │   ├── Course.java
│   │   │               │   ├── Department.java
│   │   │               │   ├── Student.java
│   │   │               │   └── Teacher.java
│   │   │               ├── repository
│   │   │               │   └── ManagementRepository.java
│   │   │               ├── security
│   │   │               │   ├── SecurityConfig.java
│   │   │               │   └── UserAccount.java
│   │   │               ├── service
│   │   │               │   ├── ManagementService.java
│   │   │               │   ├── UserDetailsServiceImpl.java
│   │   │               │   └── UserService.java
│   │   │               └── StudentManagementSystemApplication.java
│   │   └── resources
│   │       ├── application-dev.properties
│   │       ├── application.properties
│   │       ├── static
│   │       │   └── css
│   │       │       └── compact.css
│   │       └── templates
│   │           ├── access-denied.html
│   │           ├── dashboard.html
│   │           ├── entities
│   │           │   ├── entity.html
│   │           │   ├── list.html
│   │           │   └── rows.html
│   │           ├── fragments
│   │           │   ├── entity-list.html
│   │           │   ├── layout.html
│   │           │   └── navbar.html
│   │           ├── home.html
│   │           ├── login.html
│   │           ├── profile
│   │           │   └── view.html
│   │           └── register
│   │               ├── choose-role.html
│   │               ├── student.html
│   │               └── teacher.html
│   └── test
│       ├── java
│       │   └── com
│       │       └── assignment
│       │           └── studentmanagementsystem
│       │               ├── controller
│       │               │   ├── HomeControllerIntegrationTest.java
│       │               │   ├── ManagementControllerIntegrationTest.java
│       │               │   └── RegistrationControllerIntegrationTest.java
│       │               ├── repository
│       │               │   └── ManagementRepositoryIntegrationTest.java
│       │               └── service
│       │                   ├── ManagementServiceTest.java
│       │                   ├── UserDetailsServiceImplTest.java
│       │                   └── UserServiceTest.java
│       └── resources
└── target
    ├── classes
    │   ├── application-dev.properties
    │   ├── application.properties
    │   ├── com
    │   │   └── assignment
    │   │       └── studentmanagementsystem
    │   │           ├── controller
    │   │           │   ├── HomeController.class
    │   │           │   ├── ManagementController.class
    │   │           │   └── RegistrationController.class
    │   │           ├── DataLoader.class
    │   │           ├── model
    │   │           │   ├── Course.class
    │   │           │   ├── Department.class
    │   │           │   ├── Student.class
    │   │           │   └── Teacher.class
    │   │           ├── repository
    │   │           │   └── ManagementRepository.class
    │   │           ├── security
    │   │           │   ├── SecurityConfig.class
    │   │           │   ├── UserAccount$Role.class
    │   │           │   └── UserAccount.class
    │   │           ├── service
    │   │           │   ├── ManagementService.class
    │   │           │   ├── UserDetailsServiceImpl.class
    │   │           │   └── UserService.class
    │   │           └── StudentManagementSystemApplication.class
    │   ├── static
    │   │   └── css
    │   │       └── compact.css
    │   └── templates
    │       ├── access-denied.html
    │       ├── dashboard.html
    │       ├── entities
    │       │   ├── entity.html
    │       │   ├── list.html
    │       │   └── rows.html
    │       ├── fragments
    │       │   ├── entity-list.html
    │       │   ├── layout.html
    │       │   └── navbar.html
    │       ├── home.html
    │       ├── login.html
    │       ├── profile
    │       │   └── view.html
    │       └── register
    │           ├── choose-role.html
    │           ├── student.html
    │           └── teacher.html
    ├── generated-sources
    │   └── annotations
    ├── generated-test-sources
    │   └── test-annotations
    ├── maven-status
    │   └── maven-compiler-plugin
    │       ├── compile
    │       │   └── default-compile
    │       │       ├── createdFiles.lst
    │       │       └── inputFiles.lst
    │       └── testCompile
    │           └── default-testCompile
    │               ├── createdFiles.lst
    │               └── inputFiles.lst
    ├── surefire-reports
    │   ├── com.assignment.studentmanagementsystem.controller.HomeControllerIntegrationTest.txt
    │   ├── com.assignment.studentmanagementsystem.controller.ManagementControllerIntegrationTest.txt
    │   ├── com.assignment.studentmanagementsystem.controller.RegistrationControllerIntegrationTest.txt
    │   ├── com.assignment.studentmanagementsystem.repository.ManagementRepositoryIntegrationTest.txt
    │   ├── com.assignment.studentmanagementsystem.service.ManagementServiceTest.txt
    │   ├── com.assignment.studentmanagementsystem.service.UserDetailsServiceImplTest.txt
    │   ├── com.assignment.studentmanagementsystem.service.UserServiceTest.txt
    │   ├── TEST-com.assignment.studentmanagementsystem.controller.HomeControllerIntegrationTest.xml
    │   ├── TEST-com.assignment.studentmanagementsystem.controller.ManagementControllerIntegrationTest.xml
    │   ├── TEST-com.assignment.studentmanagementsystem.controller.RegistrationControllerIntegrationTest.xml
    │   ├── TEST-com.assignment.studentmanagementsystem.repository.ManagementRepositoryIntegrationTest.xml
    │   ├── TEST-com.assignment.studentmanagementsystem.service.ManagementServiceTest.xml
    │   ├── TEST-com.assignment.studentmanagementsystem.service.UserDetailsServiceImplTest.xml
    │   └── TEST-com.assignment.studentmanagementsystem.service.UserServiceTest.xml
    └── test-classes
        └── com
            └── assignment
                └── studentmanagementsystem
                    ├── controller
                    │   ├── HomeControllerIntegrationTest.class
                    │   ├── ManagementControllerIntegrationTest.class
                    │   └── RegistrationControllerIntegrationTest.class
                    ├── repository
                    │   └── ManagementRepositoryIntegrationTest.class
                    └── service
                        ├── ManagementServiceTest.class
                        ├── UserDetailsServiceImplTest.class
                        └── UserServiceTest.class
