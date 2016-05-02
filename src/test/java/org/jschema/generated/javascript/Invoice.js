var Invoice = {
  create: function(){
    return{
      jschema: {
        id: "@string",
        created_at: "@date",
        updated_at: "@date",
        email: "@string",
        total: "@number",
        subtotal: "@number",
        tax: "@number",
        notes: "@string",
        customer: {
          email: "@string",
          first_name: "@string",
          last_name: "@string",
          },
        to_address: {
          address1: "@string",
          address2: "@string",
          zip: "@string",
          state: "@string",
          country: "@string",
          },
        line_items: [
        {
        sku: "@string",
        description: "@string",
        notes: "@string",
        count: "@int",
        price: "@number",
        subtotal: "@number",
        },
        ]
    },
      validate: function(strict){
        var validators = {};
        var msg = "";
        validators["id"] = function(value){
          if(Object.prototype.toString.call(value).slice(8, -1) === 'String'){
            this.id = value;
            return "";
          }
          return "id=" + value + " does not conform to @string\n";
        };
        validators["created_at"] = function(value){
          if(Date.parse(value)===Date.parse(value)){
            this.created_at = value;
            return "";
          }
          return "created_at=" + value + " does not conform to @date\n";
        };
        validators["updated_at"] = function(value){
          if(Date.parse(value)===Date.parse(value)){
            this.updated_at = value;
            return "";
          }
          return "updated_at=" + value + " does not conform to @date\n";
        };
        validators["email"] = function(value){
          if(Object.prototype.toString.call(value).slice(8, -1) === 'String'){
            this.email = value;
            return "";
          }
          return "email=" + value + " does not conform to @string\n";
        };
        validators["total"] = function(value){
          if(Object.prototype.toString.call(value).slice(8, -1) === 'Number'){
            this.total = value;
            return "";
          }
          return "total=" + value + " does not conform to @number\n";
        };
        validators["subtotal"] = function(value){
          if(Object.prototype.toString.call(value).slice(8, -1) === 'Number'){
            this.subtotal = value;
            return "";
          }
          return "subtotal=" + value + " does not conform to @number\n";
        };
        validators["tax"] = function(value){
          if(Object.prototype.toString.call(value).slice(8, -1) === 'Number'){
            this.tax = value;
            return "";
          }
          return "tax=" + value + " does not conform to @number\n";
        };
        validators["notes"] = function(value){
          if(Object.prototype.toString.call(value).slice(8, -1) === 'String'){
            this.notes = value;
            return "";
          }
          return "notes=" + value + " does not conform to @string\n";
        };
          validators["customer"] = function(value){
            var validators={};
            var msg="";
            if(Object.prototype.toString.call(value).slice(8, -1) === 'Object'){
              this.customer = value;
            }else{
              return "customer =" + value + " does not conform to [object Object]\n";
            }
            validators["email"] = function(value){
              if(Object.prototype.toString.call(value).slice(8, -1) === 'String'){
                this.email = value;
                return "";
              }
              return "email=" + value + " does not conform to @string\n";
            };
            validators["first_name"] = function(value){
              if(Object.prototype.toString.call(value).slice(8, -1) === 'String'){
                this.first_name = value;
                return "";
              }
              return "first_name=" + value + " does not conform to @string\n";
            };
            validators["last_name"] = function(value){
              if(Object.prototype.toString.call(value).slice(8, -1) === 'String'){
                this.last_name = value;
                return "";
              }
              return "last_name=" + value + " does not conform to @string\n";
            };
            for(var key in validators){
              if(value[key]){
                msg += validators[key](value[key]);
              }
            }
            if(msg === ""){
              return "";
            }
            return msg;
          };
          validators["to_address"] = function(value){
            var validators={};
            var msg="";
            if(Object.prototype.toString.call(value).slice(8, -1) === 'Object'){
              this.to_address = value;
            }else{
              return "to_address =" + value + " does not conform to [object Object]\n";
            }
            validators["address1"] = function(value){
              if(Object.prototype.toString.call(value).slice(8, -1) === 'String'){
                this.address1 = value;
                return "";
              }
              return "address1=" + value + " does not conform to @string\n";
            };
            validators["address2"] = function(value){
              if(Object.prototype.toString.call(value).slice(8, -1) === 'String'){
                this.address2 = value;
                return "";
              }
              return "address2=" + value + " does not conform to @string\n";
            };
            validators["zip"] = function(value){
              if(Object.prototype.toString.call(value).slice(8, -1) === 'String'){
                this.zip = value;
                return "";
              }
              return "zip=" + value + " does not conform to @string\n";
            };
            validators["state"] = function(value){
              if(Object.prototype.toString.call(value).slice(8, -1) === 'String'){
                this.state = value;
                return "";
              }
              return "state=" + value + " does not conform to @string\n";
            };
            validators["country"] = function(value){
              if(Object.prototype.toString.call(value).slice(8, -1) === 'String'){
                this.country = value;
                return "";
              }
              return "country=" + value + " does not conform to @string\n";
            };
            for(var key in validators){
              if(value[key]){
                msg += validators[key](value[key]);
              }
            }
            if(msg === ""){
              return "";
            }
            return msg;
          };
        validators["line_items"] = function(value){
          if(Object.prototype.toString.call(value).slice(8, -1) === 'Array'){
            for (var elem in value){
          var validators={};
          var msg="";
          if(Object.prototype.toString.call(value[elem]).slice(8, -1) === 'Object'){
            this.line_items = value;
          }else{
            return "line_items =" + value + " does not conform to [[object Object]]\n";
          }
          validators["sku"] = function(value){
            if(Object.prototype.toString.call(value).slice(8, -1) === 'String'){
              this.sku = value;
              return "";
            }
            return "sku=" + value + " does not conform to @string\n";
          };
          validators["description"] = function(value){
            if(Object.prototype.toString.call(value).slice(8, -1) === 'String'){
              this.description = value;
              return "";
            }
            return "description=" + value + " does not conform to @string\n";
          };
          validators["notes"] = function(value){
            if(Object.prototype.toString.call(value).slice(8, -1) === 'String'){
              this.notes = value;
              return "";
            }
            return "notes=" + value + " does not conform to @string\n";
          };
          validators["count"] = function(value){
            if(Object.prototype.toString.call(value).slice(8, -1) === 'Number' && value%1===0){
              this.count = value;
              return "";
            }
            return "count=" + value + " does not conform to @int\n";
          };
          validators["price"] = function(value){
            if(Object.prototype.toString.call(value).slice(8, -1) === 'Number'){
              this.price = value;
              return "";
            }
            return "price=" + value + " does not conform to @number\n";
          };
          validators["subtotal"] = function(value){
            if(Object.prototype.toString.call(value).slice(8, -1) === 'Number'){
              this.subtotal = value;
              return "";
            }
            return "subtotal=" + value + " does not conform to @number\n";
          };
          for(var key in validators){
            if(value[elem][key]|| Object.prototype.toString.call(value[elem][key] ).slice(8, -1) === 'Boolean'){
              msg += validators[key](value[elem][key]);
            }
          }
          if(msg !== ""){
            return msg;
          }
          }
        if(msg === ""){
          return "";
        }
        return msg;
          this.line_items = value;
          return "";
        }else{
          return "line_items=" + value + " does not conform to [array]\n";
        }
      };
        for(var key in validators){
          if(strict){
            if(this.jschema[key]){
              msg += validators[key](this[key]);
            }
          }else{
            if(this[key]){
              msg += validators[key](this[key]);
            }
          }
        }
        if(msg === ""){
          return "Valid";
        }
        return msg;
      },
      toJSON: function(){
        var toJson = {};
        for (var key in this){
          if (this.hasOwnProperty(key) && key!=="jschema" &&
            Object.prototype.toString.call(this[key]).slice(8, -1) !== 'Function') {
            toJson[key] = this[key];
          }
        }
        return JSON.stringify(toJson);
      }
    };
  },
  parse: function(jsonData){
    var json;
    if(typeof jsonData != 'undefined'){
      try{
        json = JSON.parse(jsonData);
      }catch(e){
        return "Invalid JSON format";
      }
      var obj=this.create();
      for (var key in obj){
        if(obj.hasOwnProperty(key)) json[key]=obj[key];
      }
      return json;
    }
  }
};
