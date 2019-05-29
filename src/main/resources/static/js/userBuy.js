var tmp
$(document).ready(function () {
    getMovieList();


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
                 var button=document.createElement("input");
                button.type = "button" ;
                 button.value = "按钮" ;
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
                        + "<td>" + '<button type="button" style="height:30px;width:50px;">完成付款</button> '+ "</td>"
                        + " </tr>";}
                    else if(ticket.state==1){
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
                        + "<td>" + '<button type="button" style="height:30px;width:50px;">退票</button> '+ "</td>"
                        + "<td>" + button + "</td>"
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

});

