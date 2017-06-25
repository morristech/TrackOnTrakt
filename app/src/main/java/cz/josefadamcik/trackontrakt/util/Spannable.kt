// Copyright 2014 Robert Carr
// Copyright 2016 Alexandr Romanishin
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package cz.josefadamcik.trackontrakt.util

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import timber.log.Timber
import java.util.*


fun spannable(init: SpanWithChildren.() -> Unit): SpanWithChildren {
    val spanWithChildren = SpanWithChildren()
    spanWithChildren.init()
    return spanWithChildren
}

abstract class Span {
    abstract fun render(builder: SpannableStringBuilder)

    fun toCharSequence(): SpannableStringBuilder {
        val builder = SpannableStringBuilder()
        render(builder)
        return builder
    }
}

class SpanWithChildren(val what: Any? = null) : Span() {
    val children = ArrayList<Span>()

    fun color(color: Int, init: SpanWithChildren.() -> Unit): SpanWithChildren = span(ForegroundColorSpan(color), init)


    fun roundedBg(bgColor: Int, color: Int, init: SpanWithChildren.() -> Unit): SpanWithChildren = span(RoundedBackgroundSpan(bgColor, color), init)

    fun clickable(onClick: () -> Unit, init: SpanWithChildren.() -> Unit): SpanWithChildren = span(object : ClickableSpan() {
        override fun onClick(widget: View?) {
            Timber.d("onClick click on span")
            onClick()
        }
    }, init)


    fun typeface(typeface: Int, init: SpanWithChildren.() -> Unit): SpanWithChildren =
        span(StyleSpan(typeface), init)


    fun span(what: Any, init: SpanWithChildren.() -> Unit): SpanWithChildren {
        var child = SpanWithChildren(what)
        child.init()
        children.add(child)
        return this
    }

    operator fun String.unaryPlus() {
        children.add(SpanWithText(this))
    }

    override fun render(builder: SpannableStringBuilder) {
        val start = builder.length

        for (c in children) {
            c.render(builder)
        }

        if (what != null) {
            builder.setSpan(what, start, builder.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        }
    }
}

class SpanWithText(val content: Any) : Span() {
    override fun render(builder: SpannableStringBuilder) {
        builder.append(content.toString())
    }
}