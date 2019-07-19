var app = angular.module('myApp', ['ui.router', 'toastr' ]);

app.run(function($rootScope, $state, $stateParams) {
	$rootScope.$state = $state;
	$rootScope.$stateParams = $stateParams;
});

app.config(function($stateProvider, $urlRouterProvider) {
	//page entrer
	$urlRouterProvider.otherwise('/index');
	$stateProvider.state('index', {
		//accueil
		url : '/index',
		views : {
			'' : {
				templateUrl : 'partials/entree.html'
			},
			//navbar@index: partie supérieure
			'navbar@index' : {
				//menu
				templateUrl : 'partials/navbar.html',
				controller : NavbarCtrl
			},
			//home@index:partie inférieure
			'home@index' : {
				//acueil
				templateUrl : 'partials/home.html',
				controller : HomeCtrl
			}
		}
	}).state('index.Sell', {
		url : '/Sell',
		views : {
			//sellPage
			'home@index' : {
				templateUrl : 'partials/sell.html',
				controller : SellCtrl
			}
		}
	}).state('index.search', {
		//searchPage
		url : '/search',
		views : {
			'home@index' : {
		templateUrl : 'partials/search.html',
		controller : SearchCtrl
	}
	}
	}).state('index.myInfo', {
		//myInfo
		url : '/myInfo',
		views : {
			'home@index' : {
		templateUrl : 'partials/myInfo.html',
		controller : InfoCtrl
	}
	}
	}).state('index.buy', {
		//buy
		url : '/buy.html?tid',
		views : {
			'home@index' : {
		templateUrl : 'partials/buy.html',
		controller : BuyCtrl
	}
	}
	}).state('index.sellOrder', {
		//sellOrder
		url : '/sellOrder',
		views : {
			'home@index': {
				templateUrl: 'partials/sellOrder.html',
				controller: MySellOrderCtrl
			}
		}
	}).state('index.order', {
		//order
		url : '/order',
		views : {
			'home@index' : {
				templateUrl : 'partials/myorder.html',
				controller : MyOrderCtrl
			}
		}
	});
});
