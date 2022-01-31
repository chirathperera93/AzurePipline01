package com.ayubo.life.ayubolife.lifeplus.MembershipPrivilege

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.ayubo.life.ayubolife.R
import com.ayubo.life.ayubolife.prochat.common.inflate
import kotlinx.android.synthetic.main.membership_detail_card.view.*

class PrivilegeRecyclerAdapter(val membershipDetailCardArrayList: ArrayList<MembershipDetailCard>, var clickListner: OnPrivilegeItemClickListner) : RecyclerView.Adapter<PrivilegeRecyclerAdapter.PrivilegeDetailHolder>() {



    override fun getItemCount(): Int {
        return membershipDetailCardArrayList.size
    }


    override fun onBindViewHolder(holder: PrivilegeDetailHolder, position: Int) {
        val itemMembershipDetail = membershipDetailCardArrayList[position]
        holder.bindNews(itemMembershipDetail)
        holder.initialize( itemMembershipDetail, clickListner)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrivilegeDetailHolder {
        val inflatedView = parent.inflate(R.layout.membership_detail_card)
        return PrivilegeDetailHolder(inflatedView, membershipDetailCardArrayList)
    }

    class PrivilegeDetailHolder(v: View, val membershipDetailCardArrayList: ArrayList<MembershipDetailCard>) : RecyclerView.ViewHolder(v) {

        private var view: View = v
        private var membershipDetailCard: MembershipDetailCard? = null

        fun initialize(item: MembershipDetailCard, action: OnPrivilegeItemClickListner){


            view.setOnClickListener{
                action.onItemClick(item, adapterPosition)

            }

        }


//        init {
//            v.setOnClickListener {
//
//                val position: Int = adapterPosition;
//
//                System.out.println(position)
//                val membershipDetailCard: MembershipDetailCard = membershipDetailCardArrayList.get(position)
//
//                val baseActivity = BaseActivity();
//                baseActivity.processAction(membershipDetailCard.action, membershipDetailCard.meta)
//
//                System.out.println(membershipDetailCard.action)
//                System.out.println(membershipDetailCard.meta)
//            }
//        }


        fun bindNews(membershipDetailCard: MembershipDetailCard) {
            this.membershipDetailCard = membershipDetailCard
            view.membership_detail_header.text = membershipDetailCard.heading;
            view.membership_detail_sub_header.text = membershipDetailCard.text;
        }


    }

    interface OnPrivilegeItemClickListner {
          fun onItemClick(item: MembershipDetailCard, position: Int)
    }
}