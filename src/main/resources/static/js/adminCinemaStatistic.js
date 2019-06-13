var tmp;

$(document).ready(function () {


    getScheduleRate();

    getBoxOffice();

    getAudiencePrice();

    getPlacingRate();

    getPolularMovie();

    getTop10LikeMovie();

    function getScheduleRate() {

        getRequest(
            '/statistics/scheduleRate',
            function (res) {
                var data = res.content || [];
                var tableData = data.map(function (item) {
                    return {
                        value: item.time,
                        name: item.name
                    };
                });
                var nameList = data.map(function (item) {
                    return item.name;
                });
                var option = {
                    title: {
                        text: '今日排片率',
                        subtext: new Date().toLocaleDateString(),
                        x: 'center'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        x: 'center',
                        y: 'bottom',
                        data: nameList
                    },
                    toolbox: {
                        show: true,
                        feature: {
                            mark: { show: true },
                            dataView: { show: true, readOnly: false },
                            magicType: {
                                show: true,
                                type: ['pie', 'funnel']
                            },
                            restore: { show: true },
                            saveAsImage: { show: true }
                        }
                    },
                    calculable: true,
                    series: [
                        {
                            name: '面积模式',
                            type: 'pie',
                            radius: [30, 110],
                            center: ['50%', '50%'],
                            roseType: 'area',
                            data: tableData
                        }
                    ]
                };
                var scheduleRateChart = echarts.init($("#schedule-rate-container")[0]);
                scheduleRateChart.setOption(option);
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    }

    function getBoxOffice() {

        getRequest(
            '/statistics/boxOffice/total',
            function (res) {
                var data = res.content || [];
                var tableData = data.map(function (item) {
                    return item.boxOffice;
                });
                var nameList = data.map(function (item) {
                    return item.name;
                });
                var option = {
                    title: {
                        text: '所有电影票房',
                        subtext: '截止至' + new Date().toLocaleDateString(),
                        x: 'center'
                    },
                    xAxis: {
                        type: 'category',
                        data: nameList
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [{
                        data: tableData,
                        type: 'bar'
                    }]
                };
                var scheduleRateChart = echarts.init($("#box-office-container")[0]);
                scheduleRateChart.setOption(option);
            },
            function (error) {
                alert(JSON.stringify(error));
            });
    }

    function getAudiencePrice() {
        getRequest(
            '/statistics/audience/price',
            function (res) {
                var data = res.content || [];
                var tableData = data.map(function (item) {
                    return item.price;
                });
                var nameList = data.map(function (item) {
                    return formatDate(new Date(item.date));
                });
                var option = {
                    title: {
                        text: '每日客单价',
                        x: 'center'
                    },
                    xAxis: {
                        type: 'category',
                        data: nameList
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [{
                        data: tableData,
                        type: 'line'
                    }]
                };
                var scheduleRateChart = echarts.init($("#audience-price-container")[0]);
                scheduleRateChart.setOption(option);
            },
            function (error) {
                alert(JSON.stringify(error));
            });
    }

    function getPlacingRate() {
        // todo
        var date = new Date()
        getRequest(
            `/statistics/PlacingRate?date=${date.toISOString().split("T")[0]}`,
            function (res) {
                var data = res.content || [];
                tmp = res.content;
                var tableData = data.rates;
                var nameList = data.movies.map(function (movie) {
                    return movie.name;
                });
                var option = {
                    title: {
                        text: '所有电影排片率',
                        subtext: '截止至' + date.toLocaleDateString(),
                        x: 'center'
                    },
                    xAxis: {
                        type: 'category',
                        data: nameList
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [{
                        data: tableData,
                        type: 'bar'
                    }]
                };
                var scheduleRateChart = echarts.init($("#place-rate-container")[0]);
                scheduleRateChart.setOption(option);
                $("#place-rate-container").prepend("请输入要获得的上座率的日期"+'<input type="text" name="date" value="2019-05-27">'+'<button type="button" style="height:30px;width:80px;">提交</button> ');
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        )
    }

    function getPolularMovie() {
        // todo
        var days = 7;
        var movieNum = 7;
        getRequest(
            `/statistics/popular/movie?days=${days}&&movieNum=${movieNum}`,
            function (res) {
                var data = res.content || [];
                var tableData = data.map(function (item) {
                        return item.boxOffice;
                });
                var nameList = data.map(function (item) {
                    return item.name;
                });
                var option = {
                    title: {
                        text: '最受欢迎',
                        x: 'center'
                    },
                    xAxis: {
                        type: 'category',
                        data: nameList
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [{
                        data: tableData,
                        type: 'bar'
                    }]
                };
                var scheduleRateChart = echarts.init($("#popular-movie-container")[0]);
                scheduleRateChart.setOption(option);
                $("#popular-movie-container").prepend("请输入天数"+'<input type="text" name="dates" value="7">'+'<button type="button" style="height:30px;width:80px;">提交</button> ');
                $("#popular-movie-container").prepend("请输入要获得排名数量"+'<input type="text" name="date" value="7">');
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        )
    }

    function getTop10LikeMovie() {

        getRequest(
            '/movieLike/top10',
            function (res) {
                var data = res.content || [];
                var tableData = data.map(function (item) {
                    return item.likeCount;
                });
                var nameList = data.map(function (item) {
                    return item.name;
                });
                var option = {
                    title: {
                        text: '喜欢人数排名前十的电影',
                        subtext: '截止至' + new Date().toLocaleDateString(),
                        x: 'center'
                    },
                    xAxis: {
                        type: 'category',
                        data: nameList
                    },
                    yAxis: {
                        type: 'value'
                    },
                    series: [{
                        data: tableData,
                        type: 'bar'
                    }]
                };
                var top10LikeMovieChart = echarts.init($("#top10-like-movie-container")[0]);
                top10LikeMovieChart.setOption(option);
            },
            function (error) {
                alert(JSON.stringify(error));
            });
    }
});