$(document).ready(function () {

    var movieId = parseInt(window.location.href.split('?')[1].split('&')[0].split('=')[1]);
    var userId = sessionStorage.getItem('id');
    var isLike = false;
    var startDate;

    getMovie();
    if (sessionStorage.getItem('role') === 'admin')
        getMovieLikeChart();

    function getMovieLikeChart() {
        getRequest(
            '/movie/' + movieId + '/like/date',
            function (res) {
                var data = res.content,
                    dateArray = [],
                    numberArray = [];
                data.forEach(function (item) {
                    dateArray.push(item.likeTime);
                    numberArray.push(item.likeNum);
                });

                var myChart = echarts.init($("#like-date-chart")[0]);

                // 指定图表的配置项和数据
                var option = {
                    title: {
                        text: '想看人数变化表'
                    },
                    xAxis: {
                        type: 'category',
                        data: dateArray
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [{
                        data: numberArray,
                        type: 'line'
                    }]
                };

                // 使用刚指定的配置项和数据显示图表。
                myChart.setOption(option);
            },
            function (error) {
                alert(error);
            }
        );
    }

    function getMovie() {
        getRequest(
            '/movie/' + movieId + '/' + userId,
            function (res) {
                var data = res.content;
                isLike = data.islike;
                repaintMovieDetail(data);
            },
            function (error) {
                alert(error);
            }
        );
    }

    function repaintMovieDetail(movie) {
        !isLike ? $('.icon-heart').removeClass('error-text') : $('.icon-heart').addClass('error-text');
        $('#like-btn span').text(isLike ? ' 已想看' : ' 想 看');
        $('#movie-img').attr('src', movie.posterUrl);
        $('#movie-name').text(movie.name);
        $('#order-movie-name').text(movie.name);
        $('#movie-description').text(movie.description);
        startDate = new Date(movie.startDate);
        $('#movie-startDate').text(startDate.toLocaleDateString());
        $('#movie-type').text(movie.type);
        $('#movie-country').text(movie.country);
        $('#movie-language').text(movie.language);
        $('#movie-director').text(movie.director);
        $('#movie-starring').text(movie.starring);
        $('#movie-writer').text(movie.screenWriter);
        $('#movie-length').text(movie.length);
    }

    // user界面才有
    $('#like-btn').click(function () {
        var url = isLike ? '/movie/' + movieId + '/unlike?userId=' + userId : '/movie/' + movieId + '/like?userId=' + userId;
        postRequest(
            url,
            null,
            function (res) {
                isLike = !isLike;
                getMovie();
            },
            function (error) {
                alert(error);
            });
    });

    // admin界面才有
    $("#modify-btn").click(function () {
        $('#movie-description-input').text($('#movie-description').text());
        $('#movie-img-input').val($('#movie-img').attr('src'));
        $('#movie_language').val($('#movie-language').text());
        $('#movie_director').val($('#movie-director').text());
        $('#movie_starring').val($('#movie-starring').text());
        $('#movie_country').val($('#movie-language').text());
        $('#movie_name').val($('#movie-name').text());
        $('#movie_writer').val($('#movie-writer').text());
        $('#movie_length').val($('#movie-length').text());
        $('#movie_startDate').attr('value', startDate.toISOString().slice(0, 10));
        $('#movie_type').val($('#movie-type').text());
        $('#movieModal').modal('show');
    });

    $("#movie-form-btn").click(function () {
        var form = {
            id: movieId,
            type: $('#movie_type').val(),
            posterUrl: $('#movie-img-input').val(),
            description: $('#movie-description-input').val(),//
            language: $('#movie_language  ').val(),
            director: $('#movie_director').val(),
            starring: $('#movie_starring').val(),
            country: $('#movie_country').val(),
            screenWriter: $('#movie_writer').val(),
            name: $('#movie_name').val(),
            startDate: $('#movie_startDate').val(),
            length: $('#movie_length').val()
        };
        //todo 需要做一下表单验证？

        postRequest(
            '/movie/update',
            form,
            function (res) {
                if (res.success) {
                    getMovie();
                    $("#movieModal").modal('hide');
                } else {
                    alert(res.message);
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );

    })

    $("#delete-btn").click(function () {
        //alert('交给你们啦，下架别忘记需要一个确认提示框，也别忘记下架之后要对用户有所提示哦');
        if (confirm("确定要下架吗") == false) {
            return;
        }

        var form = {
            movieIdList: [movieId]
        };
        postRequest(
            '/movie/off/batch',
            form,
            function (res) {
                if (res.success) {
                    alert("下架成功");
                    history.go(-1);
                } else {
                    alert(res.message);
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        )
    });

});