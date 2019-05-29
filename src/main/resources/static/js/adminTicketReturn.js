$(document).ready(function () {
setPromotion();
function setPromotion(){
 getRequest(
 '/ticket/promotion/get',
 function (res) {
  var allpromotion=res.content;
  let promotion = allpromotion[0];
  $("#fulltime").val(promotion.fullTime);
   $("#parttime").val(promotion.partTime);
    $("#discount").val(promotion.discounts);
     $("#outtime").val(promotion.outTime);
 },
 function (error) {
  alert(JSON.stringify(error));
  }
 );
}
change=function (){
var full=parseInt($("#fulltime").val());
var part=parseInt($("#parttime").val());
var discount=parseFloat($("#discount").val());
var out=parseInt($("#outtime").val());
var form={
fullTime:full,
partTime:part,
discounts:discount,
outTime:out
};
 postRequest(
                '/ticket/promotion/change',
                form,
                function (res) {
                            alert("修改成功！")
                        },
                function (error) {
                            alert(JSON.stringify(error));
                }
                );

}
});