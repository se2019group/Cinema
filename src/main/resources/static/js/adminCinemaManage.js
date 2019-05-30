$(document).ready(function() {

    var canSeeDate = 0;

    getCanSeeDayNum();
    getCinemaHalls();

    function getCinemaHalls() {
        var halls = [];
        getRequest(
            '/hall/all',
            function (res) {
                halls = res.content;
                renderHall(halls);
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    }

    $("#hall-form-btn").click(function () {
            var hallForm = getHallForm();
            if(!validateHallForm(hallForm)) {
                return;
            }
            postRequest(
                '/hall/add',
                hallForm,
                function (res) {
                    $("#hallModal").modal('hide');
                    window.location.reload();
                },
                 function (error) {
                    alert(error);
                });
        });

    function getHallForm() {
            return {
                id: $('#hall-id-input').val(),
                name: $('#hall-name-input').val(),
                row: $('#hall-row-input').val(),
                column: $('#hall-column-input').val(),
            };
    }
    function validateHallForm(data) {
            var isValidate = true;
            if(!data.id) {
                isValidate = false;
                alert("影厅id不能为空")
                return isValidate;
            }
            if(!data.name) {
                isValidate = false;
                alert("影厅名不能为空")
                return isValidate;
            }
            if(!data.row) {
                isValidate = false;
                alert("影厅行不能为空")
                return isValidate;
            }
            if(!data.column) {
                isValidate = false;
                alert("影厅列不能为空")
            }
            return isValidate;
        }
    function renderHall(halls){
        $('#hall-card').empty();
        var hallDomStr = "";

        halls.forEach(function (hall) {
            var seat = "";
            for(var i =0;i<hall.row;i++){
                var temp = ""
                for(var j =0;j<hall.column;j++){
                    temp+="<div class='cinema-hall-seat'></div>";
                }
                seat+= "<div>"+temp+"</div>";
            }
            var hallDom =
                "<div class='cinema-hall'>" +
                "<div>" +
                "<span class='cinema-hall-name'>"+ hall.name +"</span>" +
                "<span class='cinema-hall-size'>"+ hall.column +'*'+ hall.row +"</span>" +
                "<span class='cinema-change'>"+ '<button type="button" style="height:30px;width:80px;">修改影厅</button> '+"</span>"+
                "</div>" +
                "<div class='cinema-seat'>" + seat +
                "</div>" +
                "</div>";
            hallDomStr+=hallDom;
        });
        $('#hall-card').append(hallDomStr);
    }

    function getCanSeeDayNum() {
        getRequest(
            '/schedule/view',
            function (res) {
                canSeeDate = res.content;
                $("#can-see-num").text(canSeeDate);
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    }

    $('#canview-modify-btn').click(function () {
       $("#canview-modify-btn").hide();
       $("#canview-set-input").val(canSeeDate);
       $("#canview-set-input").show();
       $("#canview-confirm-btn").show();
    });

    $('#canview-confirm-btn').click(function () {
        var dayNum = $("#canview-set-input").val();
        // 验证一下是否为数字
        postRequest(
            '/schedule/view/set',
            {day:dayNum},
            function (res) {
                if(res.success){
                    getCanSeeDayNum();
                    canSeeDate = dayNum;
                    $("#canview-modify-btn").show();
                    $("#canview-set-input").hide();
                    $("#canview-confirm-btn").hide();
                } else{
                    alert(res.message);
                }
            },
            function (error) {
                alert(JSON.stringify(error));
            }
        );
    })
});