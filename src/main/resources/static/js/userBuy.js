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
                        + " </tr>";
                    $('.ticket-in-table').append(ticketInfo);
                }
            }
        ).fail(function() {
            alert('Failed');
        })
    }
});

