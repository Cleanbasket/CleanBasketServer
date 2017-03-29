var storage = {
  init: function(){
    this.getMeta();
    this.load();
  },

  /* set name and price, and save datas */
  setCartItem: function(itemId) {
    if (this.datas.cart[itemId] === undefined){
      this.datas.cart[itemId] = {
        item_code: this.meta.orderItems[itemId].item_code,
        name: this.meta.orderItems[itemId].name,
        price: this.meta.orderItems[itemId].price,
        qty: 0
      };
    }

    this.save();
  },

  /* increase qty */
  increaseCartItem: function(itemId) {
    this.datas.cart[itemId].qty++;

    this.save();
  },

  /* decrease qty */
  decreaseCartItem: function(itemId) {
    if (this.datas.cart[itemId] !== undefined) {
      var qty = this.datas.cart[itemId].qty - 1;

      this.datas.cart[itemId].qty = Math.max(0, qty);
    }

    this.save();
  },

  updateTotals: function(){
    this.datas.totalItemQty = 0;
    this.datas.totalItemPrice = 0;

    for (var itemId in this.datas.cart) {
      var item = this.datas.cart[itemId];
      if (item.qty !== 0) {
        var subTotalPrice = item.qty * item.price;
        this.datas.totalItemQty += item.qty;
        this.datas.totalItemPrice += subTotalPrice;

        if (this.datas.totalItemPrice < 20000){
          this.datas.dropoff_price = 2000;
        } else {
          this.datas.dropoff_price = 0;
        }
      }
    }

    this.save();
  },

  /* sessionStorage의 'cart-db' 데이터를 가져와(getItem) this.datas에 적용하는 함수 */
  load: function() {
    var savedData = JSON.parse(sessionStorage.getItem('cart-db'));

    if (savedData === null) {
      this.datas = {
        cart: { },
        totalItemQty: 0,
        totalItemPrice: 0,
        dropoff_price: 2000
      };
    } else {
      this.datas = savedData;
    }
  },

  /* this.datas에 있는 데이터를 sessionStorage의 'cart-db'에 저장하는(setItem) 함수 */
  save: function() {
    var datas = JSON.stringify(this.datas);
    sessionStorage.setItem('cart-db', datas);
  },

  /* ajax로 this.meta 데이터를 초기 세팅하는 함수 */
  getMeta: function(){
    if(this.meta === null){
      $.ajax({                  
          type: "GET",
          // url: "http://localhost:8080/wash/item",
          url: "../item",
          dataType: "json",
          async: false,
          success: function(resData) { 
            if (resData.constant == 1){ // 1 = Success 
              storage.meta = $.parseJSON(resData.data);
            }
          }
      });
    }
  },

  /* this.meta의 'categories'를 리턴하는 함수 */
  getMetaCategories: function() {
    return this.meta.categories;
  },

  getMetaCategoryName: function(category) {
    return this.meta.categories[category - 1].name;
  },

  /* this.meta의 'orderItems'를 가져온 후, 해당 'category'에 있는 품목들을 리턴하는 함수 */
  getMetaOrderItems: function(category) {
    var result_items = [];

    for (var itemId in this.meta.orderItems) {
      var item = this.meta.orderItems[itemId];

      if (item.category === category) {
        result_items[itemId] = item;
      }
    }

    return result_items;
  },
  // /* this.datas.cart를 리턴하는 함수 */
  // getAllCartItem: function() {
  //   return this.datas.cart;
  // },

  getDatas: function(){
    return this.datas;
  },

  /* get cart item qty */
  getCartItemQty: function(itemId) {
    var itemQty;

    if (this.datas.cart[itemId] !== undefined) {
      itemQty = this.datas.cart[itemId].qty;
    } else {
      itemQty = 0;
    }
    return itemQty;
  },

  /* meta datas */
  meta: null,

  /* user db */
  datas: null
};
