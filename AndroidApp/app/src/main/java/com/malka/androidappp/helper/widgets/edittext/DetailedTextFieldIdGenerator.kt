package com.malka.androidappp.helper.widgets.edittext

class DetailedTextFieldIdGenerator {

    var viewIdstatic = 313
    var viewlIdstatic = 2313
    var viewllIdstatic = 4313
    var viewlllIdstatic = 6313
    var viewllllIdstatic = 8313
    var viewlllllIdstatic = 10313
    var viewllllllIdstatic = 12313
    var startIconStatic = 14313
    var startIconStatic2 = 14314
    var infoIconStatic = 16319
    var views = hashMapOf<Int, Int>()
    var viewsl = hashMapOf<Int, Int>()
    var viewsll = hashMapOf<Int, Int>()
    var viewslll = hashMapOf<Int, Int>()
    var viewsllll = hashMapOf<Int, Int>()
    var viewslllll = hashMapOf<Int, Int>()
    var viewsllllll = hashMapOf<Int, Int>()
    var infoIconView = hashMapOf<Int, Int>()
    var startIconView = hashMapOf<Int, Int>()
    var startIconView2 = hashMapOf<Int, Int>()
    companion object {
        var viwId = DetailedTextFieldIdGenerator()
    }

    fun getId(parentId: Int): Int {

        views[parentId]?.let {
            return it
        }

        viewIdstatic++
        views[parentId] = viewIdstatic
        return viewIdstatic
    }

    fun getlId(parentId: Int): Int {

        viewsl[parentId]?.let {
            return it
        }

        viewlIdstatic++
        viewsl[parentId] = viewlIdstatic
        return viewlIdstatic
    }

    fun getllId(parentId: Int): Int {

        viewsll[parentId]?.let {
            return it
        }

        viewllIdstatic++
        viewsll[parentId] = viewllIdstatic
        return viewllIdstatic
    }

    fun getlllId(parentId: Int): Int {

        viewslll[parentId]?.let {
            return it
        }

        viewlllIdstatic++
        viewslll[parentId] = viewlllIdstatic
        return viewlllIdstatic
    }

    fun getllllId(parentId: Int): Int {

        viewsllll[parentId]?.let {
            return it
        }

        viewllllIdstatic++
        viewsllll[parentId] = viewllllIdstatic
        return viewllllIdstatic
    }

    fun getlllllId(parentId: Int): Int {

        viewslllll[parentId]?.let {
            return it
        }

        viewlllllIdstatic++
        viewslllll[parentId] = viewlllllIdstatic
        return viewlllllIdstatic
    }

    fun getllllllId(parentId: Int): Int {

        viewsllllll[parentId]?.let {
            return it
        }

        viewllllllIdstatic++
        viewsllllll[parentId] = viewllllllIdstatic
        return viewllllllIdstatic
    }

    fun getInfoIconID(parentId: Int): Int {

        infoIconView[parentId]?.let {
            return it
        }

        infoIconStatic++
        infoIconView[parentId] = infoIconStatic
        return infoIconStatic
    }

    fun getStartIconID(parentId: Int): Int {

        startIconView[parentId]?.let {
            return it
        }

        startIconStatic++
        startIconView[parentId] = startIconStatic
        return startIconStatic
    }


    fun getStartIconIDD(parentId: Int): Int {

        startIconView2[parentId]?.let {
            return it
        }

        startIconStatic2++
        startIconView2[parentId] = startIconStatic2
        return startIconStatic2
    }
}