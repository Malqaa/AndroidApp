package com.malqaa.androidappp.newPhase.data.network.service

import com.malqaa.androidappp.newPhase.domain.models.dynamicSpecification.DynamicSpecificationResp
import com.malqaa.androidappp.newPhase.domain.models.regionsResp.RegionsResp


interface ListenerCallBack {
    fun callBackListener(isFailed :Boolean,response:Any?)
}
interface ListenerCallBackRegions{
    fun callBackListener(isFailed :Boolean,response: RegionsResp?)
}

interface ListenerCallBackNeighborhoods{
    fun callBackListenerNeighborhoods(isFailed :Boolean,response: RegionsResp?)
}

interface ListenerCallDynamicSpecification{
    fun callBackDynamicSpecification(isFailed :Boolean,response: DynamicSpecificationResp?)

}