var URL = "http://localhost:8080/WebProjekat/rest/"
var itemsArray = []
var itemsQuantityArray = []

$(document).ready(function(){
	
	
	$.ajax({
		url: URL + "user/isLogged"
	}).then(function(data){
		if(data.role == "ADMIN"){
			
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
		}else if(data.role == null){
			window.location.href = "index.html"
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
				}
					
			},
			error : function(xhr) {
				alert(xhr.responseText);
			}
			});
		
	});
	
	$(document).on('click', '#logoutTCadmin', function(e){
		e.preventDefault();
		window.location.href = "index.html"
		$.ajax({
			url: URL + "user/loggout"
		}).then(function(data){
			console.log("uspeh")
			
			console.log("uspeh")
			$('.loginNavRegistered').remove()
			$('.loginNavUnregistered').show()
			$("#getFavorites").hide()
		})
	});
	
	$(document).on('click', '#createRest', function(e){
		e.preventDefault()
		
		
		name = $('#createRestName').val()
		address = $('#createRestAddress').val()
		category = $('#createRestCategory').val()
		
		formData = JSON.stringify({
			name: name,
			address: address,
			restaurantCategory: category
		})
		
		$.ajax({
			url: URL + "restaurant/add",
			type: "POST",
			data: formData,
			contentType: "application/json",
			datatype: "json",
			success: function(data){
				
				alert("Uspesno!")
				
			}
		})
		
	});
	
	$(document).on('click', '#changeUserType', function(e){
		e.preventDefault()
		
		username = $('#changeUserTypeName').val()
		userType = $('#changeUserTypeType').val()
		
		formData = JSON.stringify({
			username: username,
			role: userType
		})
		
		$.ajax({
			url: URL + "user/changeRole",
			type: "POST",
			data: formData,
			contentType: "application/json",
			datatype: "json",
			success: function(data){
				console.log(data)
				alert("Uspesno!")
			}
		})
	})
	
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
					
					restaurant = "<div class='panel col-md-12' id='restaurant_" + data[i].id + "'>" +
									"<div class='panel-body'>" +
										"<h5 class='panel-title'>" + data[i].name + "</h5>" +
										"<h6 class='panel-subtitle mb-2 text-muted'>" + data[i].address + "</h6>" +
										"<a href='restaurant/addFavorite/" + data[i].id + "' class='card-link addFavoriteBtn'>Dodaj u omiljene</a>" + 
										" | <a href='item/inRestaurant/" + data[i].id + "' class='card-link showMeni'>  Meni</a>" +
										" | <a href='restaurant/delete/" + data[i].id + "' class='card-link deleteRestaurant'>  Obrisi</a>" +
										" | <a href='item/add/" + data[i].id + "' class='card-link addItem'>  Dodaj artikal</a>" +
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
	
	$(document).on('click', '.addItem', function(e){
		e.preventDefault();
		
		document.getElementById("inputFormCreateItem").reset();
		restId = $(this).attr('href').split('/')[2]
		$("#createItemRestaurant").val(restId)
		
		$('#createItemModal').modal()
		
	})
	
	$(document).on('click', '#createItem', function(e){
		
		restaurantId = $("#createItemRestaurant").val()
		name = $("#createItemName").val()
		price = $("#createItemPrice").val()
		amount = $("#createItemAmount").val()
		description = $("#createItemDescription").val()
		itemType = $("#createItemType").val()
		
		formData = JSON.stringify({
			restaurantId: restaurantId,
			name: name,
			price: price,
			amount: amount,
			description: description,
			itemType: itemType
		})
		
		$.ajax({
			url: URL + "item/add",
			type: "POST",
			data: formData,
			contentType: "application/json",
			datatype: "json",
			success: function(data){
				alert("Uspesno dodato!")
				console.log(data)
			}
		})
		
	})
	
	$(document).on('click', '.showMeni', function(e){
		
		e.preventDefault();
		
		$.ajax({
			url: URL + $(this).attr('href')
		}).then(function(data){
			
			$('.mainHolder').empty()
			
			for(var i = 0; i < data.length; i++){
				
				item = "<div class='panel col-md-12' id='item_" + data[i].id + "'>" +
							"<div class='panel-body'>" +
								"<h5 class='panel-title'>" + data[i].name + "</h5>" +
								"<h6 class='panel-subtitle mb-2 text-muted'>" + data[i].price + "din</h6>" +
								"<p class='panel-text'>" + data[i].description + "</p>" +
								"<p class='panel-text'>" + data[i].amount + "gr porcija</p>" +
								"<a href='item/delete/" + data[i].id + "' class='card-link deleteItem'>  Obrisi</a>" +
							"</div>" +
						"</div>"
								
				$('.mainHolder').append(item)
			}
			
		});
		
	});
	
	$(document).on('click', '.deleteRestaurant', function(e){
		e.preventDefault();
		
		$.ajax({
			url: URL + $(this).attr('href'),
			type: "DELETE",
			contentType: "application/json",
			datatype: "json",
			success: function(data){
				console.log(data)
				$('#restaurant_'+data.id).remove();
			}
		})
		
	});
	
	$(document).on('click', '.deleteItem', function(e){
		e.preventDefault();
		
		$.ajax({
			url: URL + $(this).attr('href'),
			type: "DELETE",
			contentType: "application/json",
			datatype: "json",
			success: function(data){
				console.log(data)
				$('#item_'+data.id).remove();
			}
		})
		
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