var getusers = function() {
    var guid = $(this).attr('id');
    $.ajax({
        url: "getusers",
        data: {'guid':guid},
        success: function(responseData){

            var trHTML = '';      
            
            $.each(responseData,function(i,item) {
                
            trHTML += '<tr><td>' + item.guid + '</td><td>' + item.name + '</td>';
            $.each(item.roles, function(i,role){
                trHTML += '<td>' + role + '</td>';    
            })
            
            });
                    
            $('#' + guid + '-roles').append(trHTML);
            
            return true;
        }});
}

$('.spacebutton').click(getusers);

