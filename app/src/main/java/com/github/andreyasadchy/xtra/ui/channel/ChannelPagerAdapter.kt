package com.github.andreyasadchy.xtra.ui.channel

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.github.andreyasadchy.xtra.R
import com.github.andreyasadchy.xtra.ui.chat.ChatFragment
import com.github.andreyasadchy.xtra.ui.clips.common.ClipsFragment
import com.github.andreyasadchy.xtra.ui.common.pagers.ItemAwareFragmentPagerAdapter
import com.github.andreyasadchy.xtra.ui.videos.channel.ChannelVideosFragment
import com.github.andreyasadchy.xtra.util.C

class ChannelPagerAdapter(
        private val context: Context,
        fm: FragmentManager,
        private val args: Bundle) : ItemAwareFragmentPagerAdapter(fm) {

    override fun getPageTitle(position: Int): CharSequence? {
        val id = when (position) {
            0 -> R.string.videos
            1 -> R.string.clips
//            2 -> R.string.info
            else -> R.string.chat
        }
        return context.getString(id)
    }

    override fun getItem(position: Int): Fragment {
        val fragment: Fragment = when (position) {
            0 -> ChannelVideosFragment()
            1 -> ClipsFragment()
//            2 -> ChannelInfoFragment()
            else -> return ChatFragment.newInstance(args.getString(C.CHANNEL_ID), args.getString(C.CHANNEL_LOGIN), args.getString(C.CHANNEL_DISPLAYNAME), args.getString(C.STREAM_ID))
        }
        return fragment.apply { arguments = args }
    }

//    override fun getCount(): Int = 4
    override fun getCount(): Int = 3
}
