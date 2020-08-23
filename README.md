## CustomerDetailsManagement
	Managing customer details. 

# Rest API's 
	*Find all Customer list.
	*Find Customer By ID.
	*Find Customer by First Name and/OR Last Name.
	*Update Customer.
	*Update Address.
	*Delete Customer. 

### Technology Stack

	*Spring Boot 2 (Web, Spring data, spring security are dependencies used)
	*Java 11
	*In Memory Data base (H2)
	*Embedded Tomcat

### Features
	*Used Swagger to document Rest API.
	*Swagger-UI for HTML documentation View.	
	*log4j2 for logging.
	*Using Spring Aspect for logging rest API.
	*Exception Handling using Customization. 
	*Spring Security.

###  Swagger URL 
	http://localhost:8080/swagger-ui.html

### URL's invoked from browser

	http://localhost:8080/api/customers
	
	http://localhost:8080/api/customers/{CustomerId}

	http://localhost:8080/api/searchbyname?firstName={firstname}&lastName={lastname}

	http://localhost:8080/api/customers/{CustomerId}/address
		
### Credentials and Roles 
	*ID : user
	 PSW :psw
	 Role: USER
	*ID : Admin
	 PSW: psw
	 Role: ADMIN

### Rules
	*"User" role has access to Get customers, Get customers by id , Get customers by first And Or Last Name.
	*"ADMIN" role has access to Get,Post,Put,Delete API.
	 