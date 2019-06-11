var tmp
var tocompleteid;
var order = { ticketId: [], couponId: 0 };
var coupons = [];
var isVIP = false;
var useVIP = true;
var totalcost;
var scheduleId;
var balancenow;
$(document).ready(function () {
    getMovieList();
mark=function(){
$('#mark').modal();
}

    function getMovieList() {
        getRequest(
            '/ticket/get/' + sessionStorage.getItem('id'),
            function (res) {
                renderTicketList(res.content);
            },
            function (error) {
                alert(error);
            });
    }
    completeTicket=function (id){
    getRequest(
            '/vip/' + sessionStorage.getItem('id') + '/get',
            function (res) {
                isVIP = res.success;
                useVIP = res.success;
                balancenow=res.content.balance.toFixed(2);
                if (isVIP) {
                    $('#member-balance').html("<div><b>会员卡余额：</b>" + res.content.balance.toFixed(2) + "元</div>");
                } else {
                    $("#member-pay").css("display", "none");
                    $("#nonmember-pay").addClass("active");

                    $("#modal-body-member").css("display", "none");
                    $("#modal-body-nonmember").css("display", "");
                }
            },
            function (error) {
                alert(error);
            });
    tocompleteid=id;
    getcost();
    getRequest(
             '/coupon/' + sessionStorage.getItem("id") + '/get',
             function (res) {
listcoupon(res.content);
            couponPromise=res.content;
         },
            function (error) {
                alert(error);
            });

     $('#buyModal').modal();
    }
    function listcoupon(list){
          var couponTicketStr = "";
             coupons = list;
            for (let coupon of coupons) {
                if(coupon.targetAmount<=totalcost){
                couponTicketStr += "<option>满" + coupon.targetAmount + "减" + coupon.discountAmount + "</option>"}
            }
            $('#order-coupons').html(couponTicketStr);
            if(couponTicketStr!=""){
             changeCoupon(0);
            }
            else{
            changeCoupon(-1);}
        }
    function changeCoupon(couponIndex) {
    if(couponIndex!=-1){
        order.couponId = coupons[couponIndex].id;
        var actualTotal = (parseFloat(totalcost) - parseFloat(coupons[couponIndex].discountAmount)).toFixed(2);
                totalcost=actualTotal;}
       else{
       order.couponId=-1;}
        $('#pay-amount').html("<div><b>金额：</b>" + totalcost + "元</div>");
    }
   switchPay= function (type) {
        useVIP = (type == 0);
        if (type == 0) {
            $("#member-pay").addClass("active");
            $("#nonmember-pay").removeClass("active");

            $("#modal-body-member").css("display", "");
            $("#modal-body-nonmember").css("display", "none");
        } else {
            $("#member-pay").removeClass("active");
            $("#nonmember-pay").addClass("active");

            $("#modal-body-member").css("display", "none");
            $("#modal-body-nonmember").css("display", "");
        }
    }
    deleteTicket=function (id){
             postRequest(
                    '/ticket/delete/'+ `?ticketId=${id}`,
                    {},
                    function (res) {
                        if (res.success) {
                            window.location.reload()
                        } else {
                            alert(res.message)
                        }
                    },
                    function (error) {
                        alert(JSON.stringify(error));
                    }

                );
    }

    TicketReturn=function (id){
    postRequest(
                  '/ticket/return/'+ id,
                        {},
                        function (res) {
                         if (res.content=="1"){
                             alert("退票成功");
                              window.location.reload();
                         }
                         else{
                         alert("不可退票");
                         }

                          },
                        function (error) {
                            alert(JSON.stringify(error));
                        }
                    );
    }

    cancelTicket=function(ids){
        postRequest(
            '/ticket/abolish/'+ `?ticketId=${ids}`,
            {},
            function (res) {
                if (res.success) {
                    window.location.reload()
                } else {
                    alert(res.message)
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            }

         );
    }
     function getcost(){
     getRequest(
                  '/ticket/'+tocompleteid,
                  function (res) {
                  totalcost=res.content;
              },
                 function (error) {
                     alert(error);
                 });
    getRequest(
                  '/ticket/schedule/'+tocompleteid,
                  function (res) {
                  scheduleId=res.content;
              },
                 function (error) {
                     alert(error);
                 });
     }
    // TODO:填空
    // $('.movie-on-list').empty();
    // var movieDomStr = '';
    // list.forEach(function (movie) {
    //     movie.description = movie.description || '';
    //     movieDomStr +=
    //         "<li class='movie-item card'>" +
    //         "<img class='movie-img' src='" + (movie.posterUrl || "../images/defaultAvatar.jpg") + "'/>" +
    //         "<div class='movie-info'>" +
    //         "<div class='movie-title'>" +
    //         "<span class='primary-text'>" + movie.name + "</span>" +
    //         "<span class='label "+(!movie.status ? 'primary-bg' : 'error-bg')+"'>" + (movie.status ? '已下架' : (new Date(movie.startDate)>=new Date()?'未上映':'热映中')) + "</span>" +
    //         "<span class='movie-want'><i class='icon-heart error-text'></i>" + (movie.likeCount || 0) + "人想看</span>" +
    //         "</div>" +
    //         "<div class='movie-description dark-text'><span>" + movie.description + "</span></div>" +
    //         "<div>类型：" + movie.type + "</div>" +
    //         "<div style='display: flex'><span>导演：" + movie.director + "</span><span style='margin-left: 30px;'>主演：" + movie.starring + "</span>" +
    //         "<div class='movie-operation'><a href='/user/movieDetail?id="+ movie.id +"'>详情</a></div></div>" +
    //         "</div>"+
    //         "</li>";
    // });
    // $('.movie-on-list').append(movieDomStr);
    function renderTicketList(list) {
        $('.ticket-in-table').empty();
        var stateList = ["未完成", "已完成", "已失效"];
        var promises = [];
        list.forEach(function (ticket) {
            promises.push(getDeferred("/schedule/" + ticket.scheduleId));
        });
        $.when(Promise.all(promises)).done(
            function() {
                var responses = arguments[0];
                var schedules = responses.map(each => each.content);
                for(let i=0; i < schedules.length; i++) {
                    let schedule = schedules[i];
                    let ticket = list[i];
                    if(ticket.state==0){
                    var button1 ="<a role='button' id="+ticket.id+" onclick='cancelTicket(this.id)'><i class='icon-edit'></i>取消付款</a>";
                    var button ="<a role='button' id="+ticket.id+" onclick='completeTicket(this.id)'><i class='icon-edit'></i>完成付款</a>";
                    var ticketInfo =
                        "<tr>"
                        + "<td>" + schedule.movieName + "</td>"
                        + "<td>" + schedule.hallName + "</td>"
                        + "<td>" + (ticket.rowIndex + 1) + "排" + (ticket.columnIndex + 1) + "列" + "</td>"
                        + "<td>" + schedule.startTime.split("T")[0] + " "
                        + schedule.startTime.split("T")[1].split(".")[0] + "</td>"
                        + "<td>" + schedule.endTime.split("T")[0] + " "
                        + schedule.endTime.split("T")[1].split(".")[0] + "</td>"
                        + "<td>" + stateList[ticket.state] + "</td>"
                        + "<td>" + button1 + "</td>"
                        + "<td>" + button + "</td>"
                        + " </tr>";}
                    else if(ticket.state==1){
                    var button ="<a role='button' id="+ticket.id+" onclick='deleteTicket(this.id)'><i class='icon-edit'></i>删除</a>";
                    var button1 ="<a role='button' id="+ticket.id+" onclick='TicketReturn(this.id)'><i class='icon-edit'></i>退票</a>";
                    var button2 ="<a role='button' id="+ticket.id+" onclick='markRecord(this.id)'><i class='icon-edit'></i>评价</a>";
                    var ticketInfo =
                        "<tr>"
                        + "<td>" + schedule.movieName + "</td>"
                        + "<td>" + schedule.hallName + "</td>"
                        + "<td>" + (ticket.rowIndex + 1) + "排" + (ticket.columnIndex + 1) + "列" + "</td>"
                        + "<td>" + schedule.startTime.split("T")[0] + " "
                        + schedule.startTime.split("T")[1].split(".")[0] + "</td>"
                        + "<td>" + schedule.endTime.split("T")[0] + " "
                        + schedule.endTime.split("T")[1].split(".")[0] + "</td>"
                        + "<td>" + stateList[ticket.state] + "</td>"
                        + "<td>" + button1 + "</td>"
                        + "<td>" + button + "</td>"
                        + "<td>" + button2 + "</td>"
                        + " </tr>";}
                    else if(ticket.state==2){
                    var button ="<a role='button' id="+ticket.id+" onclick='deleteTicket(this.id)'><i class='icon-edit'></i>删除</a>";
                    var ticketInfo =
                        "<tr>"
                        + "<td>" + schedule.movieName + "</td>"
                        + "<td>" + schedule.hallName + "</td>"
                        + "<td>" + (ticket.rowIndex + 1) + "排" + (ticket.columnIndex + 1) + "列" + "</td>"
                        + "<td>" + schedule.startTime.split("T")[0] + " "
                        + schedule.startTime.split("T")[1].split(".")[0] + "</td>"
                        + "<td>" + schedule.endTime.split("T")[0] + " "
                        + schedule.endTime.split("T")[1].split(".")[0] + "</td>"
                        + "<td>" + stateList[ticket.state] + "</td>"
                        + "<td>" + button + "</td>"
                        + " </tr>";}

                    $('.ticket-in-table').append(ticketInfo);
                }
            }
        ).fail(function() {
            alert('Failed');
        })
    }
    markRecord=function(id){
        ticketId=id;
        $('#mark').modal();
        }

        $("#mark-click").click(function () {
                    var markRecordForm = getMarkRecord(ticketId);
                    postRequest(
                        '/ticket/evaluate/',
                        markRecordForm,
                        function (res) {
                            if(res.success){
                                $("#mark").modal('hide');
                                window.location.reload();
                            }else{
                                alert(res.message);
                            }
                        },
                         function (error) {
                            alert(error);
                        });
                });

    function getMarkRecord(ticketId) {
                            return {
                                ticketId: ticketId,
                                mark: $('#mark-input').val(),
                                comment:$('#mark-comment-input').val(),
                            };
                    }


payConfirmClick=function () {
    if (useVIP) {
        postPayRequest();
        if(balancenow>=totalcost){
        postConsumeRequest();
        mark();}
         $("#buyModal").modal('hide');
          window.location.reload();
    } else {
        if (validateForm()) {
            if ($('#userBuy-cardNum').val() === "123123123" && $('#userBuy-cardPwd').val() === "123123") {
                postPayRequest();
                postConsumeRequest();
                mark();
                 $("#buyModal").modal('hide');
                 window.location.reload();
            } else {
                alert("银行卡号或密码错误");
            }
        }
    }

}


function postPayRequest() {
    var ticketIds = new Array();
        ticketIds.push(tocompleteid);
    var form = {
        //list of ticket Id
        ids: ticketIds,
        couponId: $("#order-coupons").children('option:selected').index()
    };
    var api;
    if (useVIP) {
        api = '/ticket/vip/buy'
    }
    else {
        api = '/ticket/buy'
    }
    api += `?ids=${form.ids.join("&ids=")}&&couponId=${form.couponId == -1 ? -1 : coupons[form.couponId].id}`;
    postRequest(
        api,
        {},
        function (res) {
            if (res.success) {
            } else {
                alert(res.message)
            }
        },
        function (error) {
            alert(JSON.stringify(error));
        }
    );
mark=function (){
 var ticketIds = new Array();
        ticketIds.push(tocompleteid);
         api =  '/ticket/price',
        api+= `?totalcost=${totalcost}&&ticketIds=${ticketIds}`;
        postRequest(
       api,
         {},
            function (res) {
                               if (res.success) {
                                } else {
                                    alert(res.message)
                                    }
                                },
                        function (error) {
                                    alert(JSON.stringify(error));
                        }
                    );

}
postConsumeRequest=function (){
        var type=0;
        var cost=0;
        if (useVIP) {
                  type=1
              }
        var form = {
                  "userid": sessionStorage.getItem("id"),
                  "type":type,
                  "amount":totalcost,
                  "scheduleId": scheduleId
              };
        postRequest(
                '/ticket/consume',
                form,
                function (res) {
                       if (res.success) {
                        } else {
                            alert(res.message)
                            }
                        },
                function (error) {
                            alert(JSON.stringify(error));
                }
            );
}
}
function validateForm() {
    var isValidate = true;
    if (!$('#userBuy-cardNum').val()) {
        isValidate = false;
        $('#userBuy-cardNum').parent('.form-group').addClass('has-error');
        $('#userBuy-cardNum-error').css("visibility", "visible");
    }
    if (!$('#userBuy-cardPwd').val()) {
        isValidate = false;
        $('#userBuy-cardPwd').parent('.form-group').addClass('has-error');
        $('#userBuy-cardPwd-error').css("visibility", "visible");
    }
    return isValidate;
}
});

