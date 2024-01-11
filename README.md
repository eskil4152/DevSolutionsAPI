# DevSolutions API / Backend

The server for DevSolutions website and services

## Parts-Explanation ##
* AccountController - Handles account-related functions, as login and register. 
* OrdersController - Handles order-related functions, as getting users order history, and order details
* AccountOrders - Middleman between users and their orders
* ProductController - Handles products, as getting products, retrieving detailed info, updating and such
* FaqController - Handles faq-related functions, as fetch faq and edit
* CheckoutController - Handles users cart, middle-man between orders and payment?
* PaymentController - Handles the payment, most likely via 3rd party

# SET COOKIE ON SECURE BEFORE PRODUCTION
### secure cookie unavailable over http on safari

## TODO
- [x] FAQ Controller
- [ ] FAQ Edit
- [ ] FAQ Edit only for Admins
- [ ] Make proper security/token checks

- [x] Add token checks for some endpoints
- [x] Properly pass token
- [x] Add accounts sql file and db

- [ ] Add orders possible
- [ ] Make orderUser table