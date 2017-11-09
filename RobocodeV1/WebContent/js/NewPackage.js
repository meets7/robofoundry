$("#submit").click(function NewPackage(){
		var packagename=document.getElementById("packagename").value;
		$.ajax({
			url: 'createpackage',
			type: 'POST',
			data: "packagename="+packagename,
			async : false,
			success : function(html) { 
				$('#statusmessage').html("Success");
				$('#statusmessage').css("background-color","#44b548");
				$('#packagename').val("");
				$('#statusmessage').delay(5000).fadeOut('slow');
			}
		});  
		event.preventDefault();
	});