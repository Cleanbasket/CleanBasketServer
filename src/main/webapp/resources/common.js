var Constant = function() {
	this.SESSION_EXPIRED = 0;
	this.SUCCESS = 1;
	this.ERROR = 2;
	this.EMAIL_ERROR = 3;
	this.PASSWORD_ERROR = 4;
	this.ACCOUNT_VALID = 5;
	this.ACCOUNT_INVALID = 6;
	this.ACCOUNT_ENABLED = 8;
	this.ACCOUNT_DISABLED = 9;
	this.ROLE_ADMIN = 10;
	this.ROLE_DELIVERER = 11;
	this.ROLE_MEMBER = 12;
	this.ROLE_INVALID = 13;
	this.IMAGE_WRITE_ERROR = 14;
	this.IMPOSSIBLE = 15;
	this.ACCOUNT_DUPLICATION = 16;
	this.SESSION_VALID = 17;
	this.PUSH_ASSIGN_PICKUP = 100;
	this.PUSH_ASSIGN_DROPOFF = 101;
	this.PUSH_SOON_PICKUP = 102
	this.PUSH_SOON_DROPOFF = 103;
	this.PUSH_ORDER_ADD = 200;
	this.PUSH_ORDER_CANCEL = 201;
	this.PUSH_PICKUP_COMPLETE = 202;
	this.PUSH_DROPOFF_COMPLETE = 203;
	this.PUSH_MEMBER_JOIN = 204;
	this.PUSH_DELIVERER_JOIN = 205;
	this.PUSH_CHANGE_ACCOUNT_ENABLED = 206;
}

if (!Array.indexOf) {
	Array.prototype.indexOf = function(obj, start) {
		for (var i = (start || 0); i < this.length; i++) {
			if (this[i] == obj) {
				return i;
			}
		}
	}
}

function webSocketIO() {
	// 본서버
	var socket = io('http://www.cleanbasket.co.kr:8000');

	// 개발서버
	// var socket = io('http://www.cleanbasket.co.kr:8001');

	return socket;
}

var logoutFlag = false;

function errorCheck(message) {
	if (message.indexOf("<!DOCTYPE html>") != -1) {
		if (logoutFlag == false) {
			logoutFlag = true;
			location.href = '../admin/login';
		}
	}
}

$(window).ready(function() {
	setInterval(function() {
		$.ajax({
			url : '../admin/refresh',
			async : true
		});
	}, 1000 * 1200);
});
