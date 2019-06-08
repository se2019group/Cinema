$(document).ready(function() {

    init();
    
    function init(){
        $('#people-select').append("<option value=0>所有用户</option>");
        $('#people-select').append("<option value=1>会员</option>");

        renderMatchedPeople();
        getCoupon();
    }

    function renderMatchedPeople(){
        getRequest(
            '/peopleMatched?symbol='+$('#people-select').val()+'&target='+$('#money-target-input').val(),
            function(res){
                var peopleMatchedList=res.content;
                for(let i=0;i<peopleMatchedList.length;i++){
                    let peopleMatched=peopleMatchedList[i];
                    var peopleInfo=
                         "<tr >"
                        + "<td>" + peopleMatched.userName + "</td>"
                        + "<td>" + peopleMatched.consumeAmount + "</td>"
                        + "<td>" + peopleMatched.chargeAmount + "</td>"
                        +'<td><button type="button" class="btn btn-primary" id="people-button'+i+'" value='+peopleMatched.userId+'>选择</button></td>'
                        +'<script>'
                            +"let peopleButton"+i+"=$('#people-button"+i+"');"
                            +"peopleButton"+i+".click(function(){"
                                +"if(peopleButton"+i+".text()==='选择'){"
                                    +"peopleButton"+i+".text('取消选择');"
                                +"}"
                                +"else{"
                                    +"peopleButton"+i+".text('选择');"
                                +"}"
                            +"})"
                        +'</script>'
                        + " </tr>";
                    $('.people-matched').append(peopleInfo);
                }
            },
            function(error){
                alert(error);
            }
        )
    }

    function getCoupon(){
         getRequest(
        '/coupon/getAll',
        function (res) {
            if (res.success) {
                var couponList = res.content;
                var couponListContent = '';
                for (let i=0; i<couponList.length; i++) {
                    coupon=couponList[i];
                    couponListContent += '<div class="col-md-6 coupon-wrapper"><div class="coupon"><div class="content">' +
                        '<div class="col-md-8 left">' +
                        '<div class="name">' +
                        coupon.name +
                        '</div>' +
                        '<div class="description">' +
                        coupon.description +
                        '</div>' +
                        '<div class="price">' +
                        '满' + coupon.targetAmount + '减' + coupon.discountAmount +
                        '</div>' +
                        '</div>' +
                        '<div class="col-md-4 right">' +
                        '<div>有效日期：</div>' +
                        '<div>' + formatDate(coupon.startTime) + ' ~ ' + formatDate(coupon.endTime) + '</div>' +
                        '</div></div></div>'+
                        '<button type="button" class="btn btn-primary" id="coupon-button'+i+'" value='+coupon.id+'>选择</button>'+
                        '<script>'+
                            "let couponButton"+i+"=$('#coupon-button"+i+"');"+
                            "couponButton"+i+".click(function(){"+
                                "if(couponButton"+i+".text()==='选择'){"+
                                    "couponButton"+i+".text('取消选择');"+
                                "}"+
                                "else{"+
                                    "couponButton"+i+".text('选择');"+
                                "}"+
                            "});"+
                        '</script>'+
                        '</div>';
                }
                $('#coupon-list').html(couponListContent);
            }
        },
        function (error) {
            alert(error);
        });
    }

    function formatDate(date) {
        return date.substring(5, 10).replace("-", ".");
    }

    function delTable(){
        $('.people-matched').html("");
    }

    $('#select-people-button').click(function(){
        delTable();
        renderMatchedPeople();
    })

    $('#coupon-give-btn').click(function(){
        var peopleList=[];
        var couponList=[];
        var peopleButton;
        var couponButton;

        for(let i=0; i<$('.people-matched')[0].childNodes.length; i++){
            peopleButton=$('#people-button'+i);
            if(peopleButton.text()==='取消选择'){
                peopleList.push(peopleButton.val());
            }
        }

        for(let i=0; i<$('#coupon-list')[0].childNodes.length; i++){
            couponButton=$('#coupon-button'+i);
            if(couponButton.text()==='取消选择'){
                couponList.push(couponButton.val());
            }
        }

        if(peopleList.length===0 || couponList.length===0){
            alert('没有选择人员或者没有选择优惠券！');
        }
        else{
            postRequest(
                '/coupon/issueCoupons?userIdList='+peopleList+'&couponIdList='+couponList,
                {},
                function(res){
                    if(res.success){
                        alert('赠送成功！');
                    }
                    else{
                        alert('失败');
                    }
                },
                function(error){
                    alert(error);
                }
            )
        }
    })
})