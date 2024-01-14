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
* AccountController - Handles account-related functions, as login and register. 
* OrdersController - Handles order-related functions, as getting users order history, and order details
* AccountOrders - Middleman between users and their orders
* ProductController - Handles products, as getting products, retrieving detailed info, updating and such
* FaqController - Handles faq-related functions, as fetch faq and edit
* CheckoutController - Handles users cart, middle-man between orders and payment?
* PaymentController - Handles the payment, most likely via 3rd party

# SET COOKIE ON SECURE BEFORE PRODUCTION #
### secure cookie unavailable over http on safari ###

## TODO ##
- [ ] Admin page
  - [ ] FAQ Edit only for Admins
  - [ ] Products edit
  - [ ] View all users / mods / admin
    - [ ] Revoke mods status
  - [ ] Update Orders status

- [ ] Payment possible
- [ ] Update payment status automatically

- [x] FAQ Controller
- [ ] FAQ Error handling

- [x] Make proper security/token checks

- [ ] Product design improvements
- [ ] View specific product
- [ ] Product error handling

- [x] Order creation
- [ ] UserOrder appropriately

- [x] Add token checks for some endpoints
- [x] Properly pass token
- [x] Add accounts sql file and db

- [x] Log out