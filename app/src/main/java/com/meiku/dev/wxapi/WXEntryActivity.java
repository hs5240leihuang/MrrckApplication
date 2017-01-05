package com.meiku.dev.wxapi;

//import com.tencent.mm.sdk.modelbase.BaseReq;
//import com.tencent.mm.sdk.modelbase.BaseResp;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

/**
 * 微信回调页面（分享，支付）
 *
 */
public class WXEntryActivity extends WXCallbackActivity {
//
//	@Override
//	public void onReq(BaseReq arg0) {
//	}
//
//	@Override
//	public void onResp(BaseResp resp) {
//	}

}

//public class WXEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler {
//    @Override
//    public void onReq(BaseReq arg0) {
//
//        switch (arg0.getType()) {
//            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
////                Log.d("微信", "ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX");
//                break;
//            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
////                Log.d("微信", "ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX");
//                break;
//            default:
//                break;
//        }
//        finish();
//    }
//    @Override
//    public void onResp(BaseResp resp) {
//        //String result = "";
//        switch (resp.errCode) {
//            case BaseResp.ErrCode.ERR_OK:
//
//                if (null == resp.openId) {
//                    //code = ((SendAuth.Resp) resp).code;
//                    // TODO 获取asscess_token
//                    // https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
////                    GetAccessToken();
//                }else{
//                    ToastUtil.showShortToast("分享成功");
//                }
//
//                break;
//            case BaseResp.ErrCode.ERR_USER_CANCEL:
//                ToastUtil.showShortToast("取消分享");
////                sendBrodcast(0);
//                break;
//            case BaseResp.ErrCode.ERR_AUTH_DENIED:
//                ToastUtil.showShortToast("认证失败");
////                sendBrodcast(0);
//                break;
//            default:
//               // result = "errcode_unknown";
////                sendBrodcast(0);
//                break;
//        }
//
//        //
//        finish();
//    }
//
//}
