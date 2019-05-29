$(document).ready(function () {
    getConsumeList();

    function getConsumeList(){
        getRequest(
            '/ticket/consume/' + sessionStorage.getItem('id'),
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
                    var typename="银行卡";
                    if(Record.type==1){
                    typename="会员卡";
                    }

                    var RecordInfo =
                        "<tr>"
                        + "<td>" + typename+ "</td>"
                        + "<td>" + Record.amount+ "</td>"
                        + "<td>" + "购买了"+Record.content+"的电影票"+ "</td>"
                        + "<td>" + Record.time.split("T")[0] + " "+ Record.time.split("T")[1].split(".")[0] + "</td>"
                        + " </tr>";
                    $('.record-in-table').append(RecordInfo);
                }
            }
});