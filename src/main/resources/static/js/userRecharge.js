$(document).ready(function () {
    getRechargeList();

    function getRechargeList(){
        getRequest(
            '/vip/record/' + sessionStorage.getItem('id'),
            function (res) {
                renderRecord(res.content);
            },
            function (error) {
                alert(error);
            });
    }
    function renderRecord(list) {
                for(let i=0; i < list.length; i++) {
                    let Record = list[i];
                    var RecordInfo =
                        "<tr>"
                        + "<td>" + Record.amount+ "</td>"
                        + "<td>" + Record.time.split("T")[0] + " "+ Record.time.split("T")[1].split(".")[0] + "</td>"
                        + " </tr>";
                    $('.record-in-table').append(RecordInfo);
                }
            }
});