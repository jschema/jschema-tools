var Shoppinglist = {
  create: function(){
    return{
      jschema: {
        storeName: "@string",
        itemsToBuy:[["@string"]]
      },
      validate: function(strict){
        var validators = {};
        var msg = "";
        validators["storeName"] = function(value){
          if(Object.prototype.toString.call(value).slice(8, -1) === 'String'){
            this.storeName = value;
            return "";
          }
          return "storeName=" + value + " does not conform to @string\n";
        };
        validators["itemsToBuy"] = function(value){
          if(Object.prototype.toString.call(value).slice(8, -1) === 'Array'){
            for (var elem in value){
              if(Object.prototype.toString.call(value[elem]).slice(8, -1) === 'Array'){
                value = value[elem];
                for (var elem in value){
                  if(Object.prototype.toString.call(value[elem]).slice(8, -1) !== 'String'){
                    return "itemsToBuy =[" + value + "] does not conform to [@string]\n";
                  }
                }
              }else{
                return "itemsToBuy=" + value + " does not conform to [array]\n";
              }
            }
            this.itemsToBuy = value;
            return "";
          }else{
            return "itemsToBuy=" + value + " does not conform to [array]\n";
          }
        };
        if (strict){
          for(var key in this){
            if(!this.jschema[key] && Object.prototype.toString.call(this[key]).slice(8, -1) !== 'Function' && key!="jschema"){
              msg += "Key not defined in JSchema. Strict flag only allows keys defined in JSchema.";
            }
          }
        }
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
