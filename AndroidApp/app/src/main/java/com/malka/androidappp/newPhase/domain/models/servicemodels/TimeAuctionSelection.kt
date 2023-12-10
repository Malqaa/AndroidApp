package com.malka.androidappp.newPhase.domain.models.servicemodels

class TimeAuctionSelection (val text:String,
                            var endTime:String,
                            val endTimeUTC:String,
                            var unitType:Int,
                            var customOption:Boolean=false,
                            var isSelect:Boolean=false)