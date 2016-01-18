$(document).ready(function(){
    
    $("#name-field").focus();
    
    $("#login-button").click(function(e){
        e.preventDefault();
        var fields = ["name","password"];
        for(var i in fields) {
            if(!$("#"+fields[i]+"-field").val().length) {
                return;
            }
        }
        $("#login-form").submit();
    });
    
});