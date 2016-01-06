$(document).ready(function(){
    
    var isEdited = false;
    var mazeFieldSize = 100;
    var fields = [];
    var userMoney = 100;
    var items = [
        {
            label:"START",
            sign:"S",
            price: 5
        },
        {
            label:"FINISH",
            sign:"F",
            price: 5
        },
        {
            label:"TRAP",
            sign:"T",
            price: 50
        },
        {
            label:"PORTAL",
            sign:"P",
            price: 100
        },
    ];
    var edgeMaterials = [
        {
            label: "empty",
            price: 0
        },
        {
            label: "wall",
            price: .25
        },
        {
            label: "door",
            price: 5
        },
        {
            label: "secret door",
            price: 70
        }
    ];
    var holdedItemIndex = -1;
    var holdedMaterialIndex = 0;
    
    /**
     * field object: {
     *      col:,
     *      row:,
     *      topEdge:,
     *      rightEdge:,
     *      bottomEdge:,
     *      leftEdge:, 
     * }
     */
    
    (function(){
        //resize();
        $("#generate-height").val("5");
        $("#generate-width").val("5");
        if(getCookie("maze")!==false) {
            fields = JSON.parse(getCookie("maze"));
            refreshBoard();
        }
        generateItems();
    })();
    
    function updatePrice() {
        var price = 0;
        for(var i=0 ; i<fields.length ; ++i) {
            for(var j=0 ; j<fields[i].length ; ++j) {
                var field = fields[i][j];
                price += edgeMaterials[field["rightEdge"]]["price"];
                price += edgeMaterials[field["bottomEdge"]]["price"];
                if(field["item"] !== false) {
                    var itemPrice = getItemPrice(field["item"]);
                    price = (itemPrice !== false) ? price+itemPrice : price ;
                }
            }
        }
        $("#current-cost").html(parseInt(price) + "/" + userMoney);
    }
    
    function getItemPrice(sign) {
        for(var i=0 ; i<items.length ; ++i) {
            var item = items[i];
            if(item["sign"] === sign) {
                return item["price"];
            }
        }
        return false;
    }
    
    $("#generate-button").click(function(){
        var width = $("#generate-width").val();
        var height = $("#generate-height").val();
        fields = [];
        for(var i=0 ; i<width ; ++i) {
            fields[fields.length] = [];
            for(var j=0 ; j<height ; ++j) {
                fields[i][fields[i].length] = {
                    col:i,
                    row:j,
                    rightEdge:0,
                    bottomEdge:0,
                    item: false
                };
            }
        }
        refreshBoard();
    });
    
    function generateItems() {
        $("#field-manager").empty();
        for(var i=0 ; i<items.length ; ++i) {
            addItemToToolbar(i);
        }
        for(var i=0 ; i<edgeMaterials.length ; ++i) {
            addEdgeMaterialToToolbar(i);
        }
    }
    
    function addEdgeMaterialToToolbar(i) {
        var material = edgeMaterials[i];
        var wrapper = $(document.createElement("div"));
        wrapper.attr("id","material-"+i);
        wrapper.attr("class","col-xs-3 material-wrapper");
        var materialDiv = $(document.createElement("div"));
        materialDiv.addClass("material");
        materialDiv.html(material.label);
        (function(i){
            materialDiv.click(function(e){
                $(".material-selected").removeClass("material-selected");
                $(e.target).addClass("material-selected");
                holdedMaterialIndex = i;
            });
        })(i);
        wrapper.append(materialDiv);
        $("#edge-manager").append(wrapper);
    }
    
    function addItemToToolbar(i) {
        var item = items[i];
        var wrapper = $(document.createElement("div"));
        wrapper.attr("id","item-"+i);
        wrapper.attr("class","col-xs-3 item-wrapper");
        var itemDiv = $(document.createElement("div"));
        itemDiv.addClass("item");
        itemDiv.html(item.label);
        (function(i){
            itemDiv.click(function(e){
                $(".item-selected").removeClass("item-selected");
                $(e.target).addClass("item-selected");
                holdedItemIndex = i;
            });
        })(i);
        wrapper.append(itemDiv);
        $("#field-manager").append(wrapper);
    }
    
    function removeItemFromToolbar(i) {
        $("#item-"+i).remove();
    }
    
    function refreshBoard() {
        var width = fields.length;
        var height = fields[0].length;
        $("#map-wrapper").empty();
        $("#map-wrapper").css("width",(width*mazeFieldSize)+"px");
        $("#map-wrapper").css("height",(height*mazeFieldSize)+"px");
        $("#map-wrapper").css("display","block");
        for(var j=0 ; j<height ; ++j) {
            var rowDiv = $(document.createElement("div"));
            rowDiv.addClass("maze-fields-row");
            rowDiv.css("width",(width*mazeFieldSize)+"px");
            rowDiv.css("height",(mazeFieldSize)+"px");
            for(var i=0 ; i<width ; ++i) {
                var div = $(document.createElement("div"));
                div.attr("id","maze-field-"+i+"-"+j);
                div.addClass("maze-field");
                if(j>0) {
                    div.append(createWallEdit(i,j,"top"));
                }
                if(j<height-1) {
                    div.append(createWallEdit(i,j,"bottom"));
                }
                if(i>0) {
                    div.append(createWallEdit(i,j,"left"));
                }
                if(i<width-1) {
                    div.append(createWallEdit(i,j,"right"));
                }
                
                var labelDiv = $(document.createElement("div"));
                labelDiv.addClass("item-label");
                div.append(labelDiv);
                if(fields[i][j]["item"] !== false) {
                    labelDiv.html(fields[i][j]["item"]);
                }
                
                //click event
                (function(i,j){
                    div.click(function(){
                        if(holdedItemIndex !== -1) {
                            fields[i][j]["item"] = items[holdedItemIndex]["sign"];
                            $(".item-selected").removeClass("item-selected");
                            holdedItemIndex = -1;
                            isEdited = true;
                        } else if(fields[i][j]["item"] !== false) {
                            fields[i][j]["item"] = false;
                            isEdited = true;
                        }
                        refreshBoard();
                    });
                })(i,j);
                setEdges(fields[i][j], div);
                rowDiv.append(div);
            }
            $("#map-wrapper").append(rowDiv);
        }
        saveState();
        updatePrice();
    }
    
    function createWallEdit(i,j,side) {
        var div = $(document.createElement("div"));
        div.addClass("maze-field-wall-edit");
        div.addClass("maze-field-wall-edit-"+side);
        div.click(function(){
            isEdited = true;
            switch(side) {
                case "top":
                    fields[i][j-1].bottomEdge = (fields[i][j-1].bottomEdge===holdedMaterialIndex)
                    ? 0 
                    : holdedMaterialIndex;
                    break;
                case "right":
                    fields[i][j].rightEdge = (fields[i][j].rightEdge===holdedMaterialIndex)
                    ? 0
                    : holdedMaterialIndex;
                    break;
                case "bottom":
                    fields[i][j].bottomEdge = (fields[i][j].bottomEdge===holdedMaterialIndex)
                    ? 0
                    : holdedMaterialIndex;
                    break;
                case "left":
                    fields[i-1][j].rightEdge = (fields[i-1][j].rightEdge===holdedMaterialIndex)
                    ? 0
                    : holdedMaterialIndex;
                    break;
            }
            refreshBoard();
            saveState();
        });
        return div;
    }
    
    function setEdges(element, div) {
        var edges = {
            rightEdge: "right",
            bottomEdge: "bottom"
        };
        for(var i in edges) {
            switch(element[i]) {
                case 0://empty
                    break;
                case 1://wall
                    div.css("border-"+edges[i],"solid 3px #000");
                    break;
                case 2://door
                    div.css("border-"+edges[i],"solid 3px #630");
                    break;
                case 3://secret door
                    div.css("border-"+edges[i],"dotted 3px #630");
                    break;
            }
        }
    }
    function getKey(obj, value) {
        for(var i in obj) {
            if(obj[i]===value) {
                return i;
            }
        }
        return false;
    }
    
    var savingEnabled = true;
    function saveState() {
        if(!savingEnabled) {
            return;
        }
        displaySave();
        setCookie("maze",JSON.stringify(fields));
        isEdited = false;
        savingEnabled = false;
        setTimeout(function(){
            savingEnabled = true;
            if(isEdited) {
                saveState();
            }
        },3000);
    }
    
    function displaySave() {
        $("#maze-saved").css("opacity",.5);
        setTimeout(function(){
            $("#maze-saved").css("opacity",0);
        },700);
    }
   
    $("#upload").click(function(){
        $.ajax({
            url: "checkMaze",
            type: "POST",
            data: {
                maze: JSON.stringify(fields)
            },
            success: function(result) {
                console.log(result);
            }
        });
    });
    
    function setCookie(name, value) {
        document.cookie = name+"="+value;
    }
    
    function getCookie(name) {
        var cookies = document.cookie.split(";");
        for(var i=0; i<cookies.length ; ++i) {
            var cookieArr = cookies[i].split("=");
            if(cookieArr[0]===name) {
                return cookieArr[1];
            }
        }
        return false;
    }
    
});