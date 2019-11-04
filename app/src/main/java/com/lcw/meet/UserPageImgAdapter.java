package com.lcw.meet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class UserPageImgAdapter extends PagerAdapter {

    ArrayList<String> datas;
    LayoutInflater inflater;
    Context context;

    public UserPageImgAdapter(ArrayList<String> datas, LayoutInflater inflater, Context context) {
        this.datas = datas;
        this.inflater = inflater;
        this.context= context;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    //아답터가 만들어낼 Page(View)객체를
    //생성하는 코드를 작성하는 메소드 (page를 만들어야 할때 자동으로 호출됨)
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {//container는 ViewPager를 참조하는 매개변수
        //instantiateItem 한번이 한 페이지를 만듬.

        View page= inflater.inflate(R.layout.userimg01,null);

        ImageView iv= page.findViewById(R.id.iv_userimg01);
       // iv.setImageResource(datas.get(position));
        Glide.with(context).load(datas.get(position)).into(iv);
        //Toast.makeText(context, "포지션"+position, Toast.LENGTH_SHORT).show();

        //기존 ListView는 return View를 해줬었다. 하지만 PagerView는 다름
        //만들어진 page(View)를 ViewPager에 붙이기...
        container.addView(page);
        // 여기선 리턴한 View객체가 저 아래 isViewFromObject()메소드에 전달됨.
        return page;

    }

    //Viewpager에서 제거해야할 page(View)를 제거할때 자동 실행되는 메소드 (page를 없애야할 상황만 알려주는 메소드)
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        //Viewpager에서 해당하는 page객체를 제거
        container.removeView((View)object);

    }

    //위 instantiateItem()메소드가 실행된 후
    //리턴된 page(View)가 ViewPager에서 현재 보여질 아이템과 같은지 검증하는 메소드
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {// view는 현재 보여질 페이지, object는 내가  return page;
        return view==object;    //이 값이 true여야 화면에 보인다.
    }
}
