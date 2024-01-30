# DevSolutions API / Backend #

The server for DevSolutions website and services

## HTTP Response Codes Used ##
* 200 - OK
* 401 - Unable to authenticate
* 403 - Authenticated, but unauthorized
* 404 - Not found
* 405 - Method not allowed, when trying to GET on POST endpoints, or other way around
* 409 - Conflict occurred, as of right now, only when registering with registered username
* 422 - Not Processable -  Data received as expected, but something still went wrong
* 501 - Not implemented

## Parts-Explanation ##
* UserController - Handles user-related functions, as login and register. 
* OrdersController - Handles order-related functions, as getting users order history, and order details
* UserOrders - Middleman between users and their orders
* ProductController - Handles products, as getting products, retrieving detailed info, updating and such
* FaqController - Handles faq-related functions, as fetch faq and edit

* CheckoutController - Handles users cart, middle-man between orders and payment?
* PaymentController - Handles the payment, most likely via 3rd party

## TODO ##
- [ ] Admin > viewAll -> Pageable
- [ ] Admin controller
  - [ ] FAQ
    - [ ] Add
    - [ ] Remove
  - [ ] Products edit
    - [x] Add products
    - [x] Edit products
    - [x] Remove products
    - [ ] Update for better efficiency and cost
  - [ ] Orders
    - [x] View all orders
    - [ ] Update orders
    - [ ] Cancel orders
  - [ ] View all users / mods
    - [x] View all users
    - [x] View all moderators
    - [x] Grant moderator status
    - [x] Revoke moderator status
    - [x] Grant admin status
    - [ ] Revoke admin status
  - [ ] Update Orders status

- [x] FAQ Controller
  - [ ] Get all
- [ ] FAQ Error handling

- [x] Order Controller
  - [x] Make new order
  - [x] Get all orders from user
  - [x] Get specific order from user

- [x] Products Controller
  - [x] View all products
  - [x] View a specific product

- [ ] User controller
  - [x] Log in
  - [x] Register
  - [x] View profile
  - [ ] Update profile

- [ ] Payment possible
- [ ] Update payment status automatically

- [ ] UserOrder appropriately

- [x] Properly pass token
- [x] Make proper security/token checks