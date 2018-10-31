var URL = "http://localhost:8080/WebProjekat/rest/"
var itemsArray = []
var itemsQuantityArray = []

$(document).ready(function(){
	
	
	$.ajax({
		url: URL + "user/isLogged"
	}).then(function(data){
		if(data.role == "BUYER"){
			
			$('.loginNavUnregistered').hide()
			$("#getFavorites").show()
			
			logout = "<li class='dropdown loginNavRegistered'>" +
			    		"<a class='dropdown-toggle' data-toggle='dropdown' href='#'>" +
			    		"<span class='loggedName'>" + data.username + "</span>" +
			        	"<span class='caret'></span>" +
			    		"</a>" +
			    		"<ul class='dropdown-menu'>" +
			    			"<li><a href='/' id='logoutTCadmin'><span class='glyphicon glyphicon-log-out'></span> Odjava</a></li>" +
			    		"</ul>" +
			    	"</li>"
			
			$('.navbar-right').append(logout)
		}else if(data.role == "ADMIN"){
			window.location.href = 'admin.html'
		}
	})
	
	/*		REGISTRACIJA		*/
	
	$("#register").click(function(){
		
		 
		 formData = JSON.stringify({
				email: $("#inputForm [name='emailRegistration']").val(),
				username: $("#inputForm [name='usernameRegistration']").val(),
				firstName: $("#inputForm [name='nameRegistration']").val(),
				lastName: $("#inputForm [name='surnameRegistration']").val(),
				password: $("#inputForm [name='passwordRegistration']").val(),
				phoneNumber: $("#inputForm [name='phoneNumberRegistration']").val(),
		 });
		 
		 var pass=$("#inputForm [name='passwordRegistration']").val();
		 var confPass=$("#inputForm [name='confirmPasswordRegistration']").val();
		 if(pass==confPass)
		 { 

				 $.ajax({
					url: URL + "user/register",
					type: "POST",
					data: formData,
					contentType: "application/json",
					datatype: "json",
					success: function(data) {
							alert("Uspesna registracija.");
					  },
					 error: function(error) {
				            alert("Email is already used!");
				        }
					});
				 
		}else{
			alert("Lozinke se ne poklapaju!");
		}
		$('#inputR').modal('toggle');
		 
	});
	
	
	/*		LOGOVANJE		*/
	$("#login").click(function(){
		
		 formData = JSON.stringify({
			username: $("#inputFormLogin [name='username']").val(),
			password: $("#inputFormLogin [name='password']").val(),
			
		 });
		$.ajax({
			
		
			url: URL + "user/login",
			type: "POST",
			data: formData,
			contentType: "application/json",
			datatype: "json",
			success: function(data) {
				
				$("#getFavorites").show()
				
				if(data.role == "BUYER"){
					
					$('.loginNavUnregistered').hide()
					
					logout = "<li class='dropdown loginNavRegistered'>" +
					    		"<a class='dropdown-toggle' data-toggle='dropdown' href='#'>" +
					    		"<span class='loggedName'>" + data.username + "</span>" +
					        	"<span class='caret'></span>" +
					    		"</a>" +
					    		"<ul class='dropdown-menu'>" +
					    			"<li><a href='/' id='logoutTCadmin'><span class='glyphicon glyphicon-log-out'></span> Odjava</a></li>" +
					    		"</ul>" +
					    	"</li>"
					
					$('.navbar-right').append(logout)
				}else if(data.role == "ADMIN"){
					window.location.href = "admin.html"
				}
					
			},
			error : function(xhr) {
				alert(xhr.responseText);
			}
			});
		
	});
	
	$(document).on('click', '#logoutTCadmin', function(e){
		e.preventDefault();
		
		$.ajax({
			url: URL + "user/loggout"
		}).then(function(data){
			$('.loginNavRegistered').remove()
			$('.loginNavUnregistered').show()
			$("#getFavorites").hide()
		})
	});
	
	$(document).on('click', '#getFavorites', function(e){
		e.preventDefault();
		
		$.ajax({
			url: URL + $(this).attr('href')
		}).then(function(data){
			
			$('.mainHolder').empty();
			
			for(var i = 0; i < data.length; i++){
				favorite = "<div class='panel col-md-12'>" +
								"<div class='panel-body'>" +
									"<h5 class='panel-title'>" + data[i].name + "</h5>" +
									"<h6 class='panel-subtitle mb-2 text-muted'>" + data[i].address + "</h6>" +
								"</div>" +
							"</div>"
								
				$('.mainHolder').append(favorite);
			}
			
		})
		
	})
	
	$('.restCategoryBtn').click(function(e){
		
		e.preventDefault();
		
		formData = JSON.stringify({
			restaurantCategory: $(this).attr('href'),
			name: "",
			address: ""
		 });
		$.ajax({
			
		
			url: URL + "restaurant/search",
			type: "POST",
			data: formData,
			contentType: "application/json",
			datatype: "json",
			success: function(data) {
				
				$('.mainHolder').empty()
				
				for(var i = 0; i < data.length; i++){
					
					restaurant = "<div class='panel col-md-12'>" +
									"<div class='panel-body'>" +
										"<h5 class='panel-title'>" + data[i].name + "</h5>" +
										"<h6 class='panel-subtitle mb-2 text-muted'>" + data[i].address + "</h6>" +
										"<a href='restaurant/addFavorite/" + data[i].id + "' class='card-link addFavoriteBtn'>Dodaj u omiljene</a>" +
										" | <a href='item/inRestaurant/" + data[i].id + "' class='card-link showMeni'>  Meni</a>" +
									"</div>" +
								"</div>"
									
					$('.mainHolder').append(restaurant)
				}
					
			},
			error : function(xhr) {
				alert(xhr.responseText);
			}
		});
		
	});
	
	$(document).on('click', '.showMeni', function(e){
		
		e.preventDefault();
		
		$.ajax({
			url: URL + $(this).attr('href')
		}).then(function(data){
			
			$('.mainHolder').empty()
			
			for(var i = 0; i < data.length; i++){
				
				item = "<div class='panel col-md-12'>" +
							"<div class='panel-body'>" +
								"<h5 class='panel-title'>" + data[i].name + "</h5>" +
								"<h6 class='panel-subtitle mb-2 text-muted'>" + data[i].price + "din</h6>" +
								"<p class='panel-text'>" + data[i].description + "</p>" +
								"<p class='panel-text'>" + data[i].amount + "gr porcija</p>" +
							"</div>" +
						"</div>"
								
				$('.mainHolder').append(item)
			}
			
		});
		
	});
	
	$(document).on('click', '.addFavoriteBtn', function(e){
		e.preventDefault();
		
		$.ajax({
			url: URL + $(this).attr('href'),
			type: "POST",
			contentType: "application/json",
			datatype: "json",
			success: function(data){
				console.log(data)
				$(this).val("uspesno")
			}
		})
		
	})
	
	$(document).on('click', '#getMostPopularBtn', function(e){
		
		e.preventDefault();
		
		$.ajax({
			url: URL + $(this).attr('href')
		}).then(function(data){
			
			$('.mainHolder').empty()
			
			for(var i = 0; i < data.length; i++){
				
				item = "<div class='panel col-md-12'>" +
							"<div class='panel-body'>" +
								"<h5 class='panel-title'>" + data[i].name + "</h5>" +
								"<h6 class='panel-subtitle mb-2 text-muted'>" + data[i].price + "din</h6>" +
								"<p class='panel-text'>" + data[i].description + "</p>" +
								"<p class='panel-text'>" + data[i].amount + "gr porcija</p>" +
							"</div>" +
						"</div>"
								
				$('.mainHolder').append(item)
			}
			
		});
		
	});
	
	$(document).on('click', '#searchRest', function(){
		
		name = $('#searchRestName').val()
		address = $('#searchRestAddress').val()
		category = $('#searchRestCategory').val()
		
		formData = JSON.stringify({
			name: name,
			address: address,
			restaurantCategory: category
		})
		
		$.ajax({
			url: URL + "restaurant/search",
			type: "POST",
			data: formData,
			contentType: "application/json",
			datatype: "json",
			success: function(data){
				

				$('.mainHolder').empty()
				
				for(var i = 0; i < data.length; i++){
					
					restaurant = "<div class='panel col-md-12'>" +
									"<div class='panel-body'>" +
										"<h5 class='panel-title'>" + data[i].name + "</h5>" +
										"<h6 class='panel-subtitle mb-2 text-muted'>" + data[i].address + "</h6>" +
										"<a href='restaurant/addFavorite/" + data[i].id + "' class='card-link addFavoriteBtn'>Dodaj u omiljene</a>"
									"</div>" +
								"</div>"
									
					$('.mainHolder').append(restaurant)
				}
				
			}
		})
		
	});
	
	
});