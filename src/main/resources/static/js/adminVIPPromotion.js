$(document).ready(function() {
    var target=$('#target');
    var discount=$('#discount');
    var change=$('#change');
    
    init();
    
    
    
    //初始化优惠信息
    function init(){
        getRequest(
            '/vipPromotion/get',

            function(res){
                target.val(""+res.content.target);
                discount.val(res.content.discount);
            },
            function(error){
                alert("初始化会员优惠信息失败！");
            }
        );
    }

    change.click(function(){
        postRequest(
            '/vipPromotion/set?target='+target.val()+'&discount='+discount.val(),
            {},
            function(res){
                alert('优惠信息修改成功!');
            },
            function(error){
                alert('设置失败');
            }
        )
    })
})