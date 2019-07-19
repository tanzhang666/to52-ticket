// navbarCtrl
function NavbarCtrl($scope, $http, $state, toastr) {

	$http({
		url : "/client/getCid"
	}).success(function(response) {
		var cid = document.getElementById('cidForAll');
		cid.innerText=response;
		console.log(cid);
	});

		$scope.user = {
					isShowLogin : true,
					isShowUser : true
				}
	// Afficher / masquer le menu déroulant
	$scope.menu_show = function() {
		$("#user_menu").show();
	}
	$scope.menu_hide = function() {
		$("#user_menu").hide();
	}
	// dialog du connexion et inscription
	$scope.login_signup = function(ls) {
		$scope.login = true;
		$scope.log = false;
		$("#login").modal();
		if (ls == "login") {
			$scope.login = true;
			$scope.user.act = "login";
		} else {
			$scope.login = false;
			$scope.user.act = "signup";
		}
	}
	// connexion et inscrire
	$scope.submits = function(action) {
		console.log(action);
		if (action != "login") {															//si il n'est pas login, inscrire 
			console.log($scope.user);
			$http.post("client/register", $scope.user).success(function(response) {   //Envoyez une demande à le back-end pour inscrire
			if (response.message == "success") {
				$('.close').click();
				$state.go('index', location.reload('index'));
			} else {
				$scope.ansInfo = response.message;
			}
		});}
		else{
			$http.post("client/login", $scope.user).success(function(response) {	//Envoyez une demande à le back-end pour connexion
			if (response.message == "success") {
				$('.close').click();
				$state.go('index', location.reload('index'));
			} else {
				$scope.ansInfo = response.message;
			}
		});
	}
	};
	// quiter
	$scope.out = function() {
		$http({
			url : "users/out"
		}).success(function(response) {
			if (response) {
				$state.go('index', location.reload('index'));
			}
		});
	};
}
// homeController
function HomeCtrl($scope, $http) {
	// Initialiser les données du ticket de la page d'accueil
	$http.post("ticket/getAllTicket").success(function(response) {  //Envoyez une demande à le back-end pour obtenir les billets
		$scope.list = true;
		var vm = $scope.vm = {};
		vm.items = response.data;																		 //Sauvegarder les données

	});

}

// my order controller
function MyOrderCtrl($scope, $http) {
	// Initialiser les données de ma page de commande
	var cid = document.getElementById("cidForAll").innerText;
	$http.get("order/getOrderByBid?bid="+cid).success(function(response) {
		var vm =$scope.vm={};
		vm.orders = response.data;
		console.log(vm.orders);
	});
}

// my sell order controller
function MySellOrderCtrl($scope, $http,$state) {
	// Initialiser les données de ma page de commande
	$http.get("order/getOrderBySid?sid="+document.getElementById("cidForAll").innerText).success(function(response) {
		$scope.orders = response.data;
		console.log(response.data);
	});
		// confirmer order
	$scope.confirm_order = function(oid,tid,sid) {
		$http.get("order/validateOrder?sid="+sid+"&tid="+tid+"&oid="+oid).success(function(response) {
			if (response.status == "success") {
				alert(response.message);
				$state.go('index', location.reload('index'));
			} else {
				alert(response.message);
			}
		});
	};
			// refuser order
	$scope.refuse_order = function(oid,tid,sid) {
		$http.get("order/rejectOrder?sid="+sid+"&tid="+tid+"&oid="+oid).success(function(response) {
			if (response.status == "success") {
				alert(response.message);
				location.reload('index.sellOrder');
			} else {
				alert(response.message);
			}
		});
	};
}

// buy controller
function BuyCtrl($scope, $http,$stateParams) {
	// Initialiser les données de ma page de acheter
	$http.get("ticket/getTicketByTid?tid="+$stateParams.tid).success(function(response) {
		$scope.ticket = response.data;
		console.log($scope.ticket);
	});
	// buy ticket et générer la commande
		$scope.buy = function() {
		//Obtenir l'ID utilisateur
		$scope.ticket.bid=document.getElementById("cidForAll").innerText;
		//Envoyer une demande à le back-end
		$http.get("order/createOrder?bid="+$scope.ticket.bid+"&tid="+$scope.ticket.tid).success(function(response) {
			if (response.status == "success") {
				alert(response.message);
				location.reload('index.home');
			} else {
				alert(response.message);
			}
		});
	};
}

function InfoCtrl($scope, $http) {
	// Initialiser les données personnelles
	var cidv=document.getElementById("cidForAll").innerText;
	$scope.Info={cid : cidv,
		lastName :"",
		firstName : "" ,
		sex : "",
		email : "",
		phone : "",
		address : "",
		password : "",
		privilege : ""};
	if($scope.Info.cid!=null){
		// envoyer request
	$http.post("client/getClientById",$scope.Info).success(function(response) {
		$scope.Info = {
			firstName : response.data.firstName,
			cid : response.data.cid,
			lastName : response.data.lastName,
			sex : response.data.sex,
			email : response.data.email,
			phone : response.data.phone,
			address : response.data.address,
			password : response.data.password,
			privilege : response.data.privilege
		}
	})};
	$scope.$watch("Info", function(newValue,oldValue){
		$scope.Info=newValue;
	});
	// Modifier les informations personnelles
	$scope.submits = function(action) {
		console.log($scope.Info);

		$http.post("client/updateClientInfo", $scope.Info).success(function(response) {
			if (response.message == "success") {
				alert(response.message);
				$scope.Info=response.data;
				location.reload('index');
			} else {
				alert(response.message);
			}
		});
	};
}

// sell Page
function SellCtrl($scope, $http) {
	$scope.submits = function() {
		// add ticket
		// obtenir id utilisateur
		var cidF = document.getElementById("cidForAll").innerText;
		$scope.sell.seller={cid : cidF};
		// Formatez la date
		var dateDepart = $scope.sell.dateDepart;
		var monD = dateDepart.getMonth()+1;
		$scope.sell.dateDepart=dateDepart.getDate()+"/"+monD+"/"+dateDepart.getFullYear();
		var dateA = $scope.sell.dateArrival;
		var monA = dateA.getMonth()+1;
		$scope.sell.dateArrival=dateA.getDate()+"/"+monA+"/"+dateA.getFullYear();
		//Envoyer une demande à l'arrière-plan.
		$http.post("ticket/createTicket", $scope.sell).success(function(response) {

			if (response.status == "success") {
				alert(response.message);
				location.reload('index.sell');
			} else {
				alert(response.message);
			}
		});
		};
}


// search Page
function SearchCtrl($scope, $http) {
	$(document).ready(function(){
	$scope.submits = function() {
		//Obtenez les données front-end.
		$scope.$watch("search", function(newValue,oldValue){
			$scope.search=newValue;
		});
		//Formatez la date
		var dateDepart = $scope.search.dateDepart;
		if(JSON.stringify(dateDepart)!=undefined&&JSON.stringify(dateDepart)!="null"){
			var monD = dateDepart.getMonth()+1;
			$scope.search.dateDeparts=dateDepart.getDate()+"/"+monD+"/"+dateDepart.getFullYear();
		}
		var dateDepartTo = $scope.search.dateDepartTo;
		if(JSON.stringify(dateDepartTo) != undefined&&JSON.stringify(dateDepartTo)!="null"){
			var monDT = dateDepartTo.getMonth()+1;
			var dateDT = dateDepartTo.getDate()+1;
			$scope.search.dateDepartTos=dateDT+"/"+monDT+"/"+dateDepartTo.getFullYear();
		}
		console.log($scope.search);
		var search = $scope.search;
		// search ticket
				$.ajax({url:"ticket/getTicketByCondition",
					        type : 'POST',
        					async : false,
        					data : search,
							contentType: "application/x-www-form-urlencoded",
        					success:function(response){
								console.log(response);
        						$("#tableContent").empty();
        						//Changer les données du formulaire.
									for (var x in response.data) {
										console.log(response.data[x].departure);
										$("#tableContent").append("<tr>");
										$("#tableContent").append($("<td></td>").text(response.data[x].departure));
										$("#tableContent").append($("<td></td>").text(response.data[x].destination));
										$("#tableContent").append($("<td></td>").text(response.data[x].price));
										$("#tableContent").append($("<td></td>").text(response.data[x].timeDepart));
										$("#tableContent").append($("<td></td>").text(response.data[x].timeArrival));
										$("#tableContent").append($("<td></td>").text(response.data[x].dateDepart));
										$("#tableContent").append($("<td></td>").text(response.data[x].dateArrival));
										$("#tableContent").append("</tr>");
									}
		}});
	};
	});
}