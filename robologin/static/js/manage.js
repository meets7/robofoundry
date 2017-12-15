var getusers = function() {
    var guid = $(this).attr('id');
    $.ajax({
        url: "getusers",
        data: {'guid':guid},
        success: function(responseData){

            var trHTML = '';      
            
            $.each(responseData,function(i,item) {
                
            trHTML += '<tr><td>' + item.name + '</td><td>' + item.guid + '</td>';
            $.each(item.roles, function(i,role){
                trHTML += '<td>' + role + '</td>';    
            })
            
            });
                    
            $('#' + guid + '-roles').append(trHTML);
            $('#' + guid + '-roles table').attr('bgcolor',"#00FF00");
            
            return true;
        }});
}

$('.spacebutton').click(getusers);

