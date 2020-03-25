package com.example.mybang;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.*;

public class Normal {
    private Activity mContext;
    private RecyclerView mRecyclerView;
    TextView textView_life; //显示的血量
    List<Card> handCardList = new ArrayList<>(); //手牌卡组
    List<Card> equipList = new ArrayList<>(); //目前装备着的卡牌
    //cardsInHand cih = new cardsInHand();
    boolean chooseYes = false; //玩家在决斗中选择用不用bang
    boolean haveShotgun = false;
    boolean canUseMoreBang = false;
    boolean haveScope = false;
    boolean haveMustang = false;
    int identity = 0; // 身份
    int location; // 桌上位置
    int attackDistance = 1; // 可攻击距离
    int defenceDistance = 0; //防守距离
    int haveBarrel = 0; //现在正装备的酒桶数量
    int round = 0; //当前回合数


    public Normal(int location, Activity mContext,RecyclerView mRecyclerView){ //构造方法
        //TODO
        this.location = location;
        this.mContext = mContext;
        this.mRecyclerView = mRecyclerView;
    }


    /* 对照表
    //现有的手牌
    class cardsInHand{
        int bang = 0;       //a2_1  25  砰！          1
        int missed = 0;     //a2_2  12  避开！         2
        int beer = 0;       //a2_3  6   啤酒          3
        int burglar = 0;    //a2_4  4   窃贼          4
        int duel = 0;       //a2_5  3   决斗          5
        int panic = 0;      //a2_6  4   恐慌          6
        int indians = 0;    //a2_7  2   印地安人入侵   7
        int store = 0;      //a2_8  2   杂货店        8
        int gatling = 0;    //a2_9  1   加特林机枪     9
        int stagecoach = 0; //a2_10 2   小马车        10
        int wellsFargo = 0; //a2_11 1   威尔士马车     11
        int salon = 0;     //a2_12  1   酒吧沙龙       12
        int shotgun = 0;    //a1_1  2   霰弹枪         13
        int rifle = 0;      //a1_2  3   步枪1         14
        int revolver = 0;   //a1_3  1   左轮枪         15
        int carbine = 0;    //a1_4  1   卡宾枪         16
        int snipingRifle = 0;//a1_5 1   狙击枪         17
        int barrel = 0;     //a1_6  2   酒桶          18
        int jail = 0;       //a1_7  3   监狱          19
        int dynamite = 0;   //a1_8  1   炸药          20
        int scope = 0;      //a1_9  1   瞄准镜         21
        int mustang;        //a1_10 2   野马          22
    }
    // 获取 现在拥有的手牌总数量
    int getNumOfCardsInHand(cardsInHand cardsinhand){
        return cardsinhand.bang+cardsinhand.missed+
                cardsinhand.beer+cardsinhand.burglar+cardsinhand.duel+cardsinhand.panic+cardsinhand.indians
                +cardsinhand.store +cardsinhand.gatling+cardsinhand.stagecoach+cardsinhand.wellsFargo
                +cardsinhand.salon +cardsinhand.shotgun+cardsinhand.rifle+cardsinhand.revolver
                +cardsinhand.carbine +cardsinhand.snipingRifle+cardsinhand.barrel+cardsinhand.jail
                +cardsinhand.dynamite +cardsinhand.scope+cardsinhand.mustang;
    }
    */

    int life = 4; //生命值
    // 获取生命
    public int getLife(){
        return this.life;
    }

    public void setTextView_life(){ //根据生命值设置布局中的血量
        textView_life.setText("血量:" + Integer.toString(life));
    }


    //设置身份
    public void setIdentity(int identity) {
        this.identity = identity;
    }
    public int getIdentify(){ //获取身份
        return this.identity;
    }



    // 抽牌函数
    public List<Card> getCard(List<Card> cardList){
        //从抽牌堆拿第一张到自己手牌卡组
        if(cardList.size()>0){
            handCardList.add(cardList.get(0));
            cardList.remove(0);
            //Log.e("handcard","***********************");
            //for(int i = 0;i<this.handCardList.size();i++){
             //   Log.e("handcard",handCardList.get(i).getName());
            //}
            this.handCardList = sortHandCardList(this.handCardList);
            //Log.e("handcard","***********************");
            //for(int i = 0;i<this.handCardList.size();i++){
             //   Log.e("handcard",handCardList.get(i).getName());
            //}
            //Log.e("handcard","***********************");
        }
        return cardList;
    }

    // 排序手牌函数（根据key）
    List<Card> sortHandCardList(List<Card> cardList){
        int len = cardList.size();
        if (len<=1) return cardList;
        for(int i = 0;i<len;i++){
            for(int j = 1;j<len-i;j++){
                if(cardList.get(j-1).getKey() > cardList.get(j).getKey()){
                    Collections.swap(cardList,j-1,j);
                }
            }
        }
        return cardList;
    }


    //Bang 1
    public void bang(Normal normal){ //bang是对normal对象使用
        for(int i = 0;i<handCardList.size();i++){ //遍历手牌，找到一张bang
            if(handCardList.get(i).getKey()==1){
                //Log.e("TEST", "handCardList.get(i) = "+handCardList.get(i).getName());
                for(Card card:handCardList){
                    Log.e("在bang函数里面，移除一张bang之前", card.getName());
                }
                handCardList.remove(i);
                handCardList = sortHandCardList(handCardList);
                for(Card card:handCardList){
                    Log.e("在bang函数里面，移除一张bang之后", card.getName());
                }
                break;
            }
        }
        normal.isBanged(); //
    }

    //isBanged 被bang
    public void isBanged(){
        boolean haveMissed = false;
        int missedIdx = -1;
        // 判断装备区有没有酒桶
        if(this.haveBarrel>0){//有酒桶，抽奖
            Random random = new Random();
            int rn = random.nextInt(4);
            if(rn == 2){//抽奖成功
                // Toast.makeText(FIirstActivity.this, "酒桶躲避成功",Toast.LENGTH_LONG).show();
                return;
            }else{
                // Toast.makeText(FIirstActivity.this, "酒桶躲避失败",Toast.LENGTH_LONG).show();
            }
        }
        // 判断手牌有没有missed
        for(int i = 0;i<handCardList.size();i++){
            if(handCardList.get(i).getKey() == 2){
                haveMissed = true;
                missedIdx = i;
                break;
            }
        }
        final int missedIdx2 = missedIdx;
        if(haveMissed) {//正常来说自己选择用不用，暂时让他有得用就用，自己可选择
            if (location != 0) { //不是玩家自己
                handCardList.remove(missedIdx); //从手牌中移除missed
                handCardList = sortHandCardList(handCardList);
                return;
            } else {//是玩家
                //这时候希望出现一个按钮使不使用missed
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("你要使用避开吗？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("使用", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handCardList.remove(missedIdx2); //从手牌中移除missed,更新手牌显示
                        handCardList = sortHandCardList(handCardList);
                        CardAdapter adapter = new CardAdapter(handCardList);
                        mRecyclerView.setAdapter(adapter);
                    }
                });
                dialog.setPositiveButton("不使用", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 扣血，更新显示
                        life--;
                        setTextView_life();
                    }
                });
                return;
            }
        }else{//没有或者不使用missed
            // 扣血，更新显示
            life--;
            setTextView_life();
        }
    }

    //啤酒 3
    public void beer(){
        for(int i = 0;i<handCardList.size();i++){
            if(handCardList.get(i).getKey()==3){
                handCardList.remove(i);
                handCardList = sortHandCardList(handCardList);
                break;
            }
        }
        life++;
        setTextView_life();
    }

    //窃贼  效果：弃置目标玩家场上的一张装备牌、陷阱牌或一张手牌。 4
    //暂时只让对方弃置其中一张手牌，没有手牌的话就作废
    public void burglar(Normal normal){
        if(normal.handCardList.size() > 0){
            Random random = new Random();
            normal.handCardList.remove(random.nextInt(handCardList.size()));
            handCardList = normal.sortHandCardList(handCardList);
        }
        for(int i = 0;i<handCardList.size();i++){
            if(handCardList.get(i).getKey()==4){
                handCardList.remove(i);
                handCardList = sortHandCardList(handCardList);
                break;
            }
        }
    }

    //决斗 5
    public void duel(Normal normal){
        boolean end = false;
        while(!end){
            end = normal.inDuel();
            if(end) return;
            end = inDuel();
            if(end) return;
        }
        for(int i = 0;i<handCardList.size();i++){
            if(handCardList.get(i).getKey()==5){
                handCardList.remove(i);
                handCardList = sortHandCardList(handCardList);
                break;
            }
        }
    }

    public boolean inDuel(){
        boolean end = false;
        boolean haveBang = false;

        int bangIdx = -1;
        // 判断手牌有没有bang
        for(int i = 0;i<handCardList.size();i++){
            if(handCardList.get(i).getKey() == 1){
                haveBang = true;
                bangIdx = i;
                break;
            }
        }
        final int bangIdx2 = bangIdx;
        if(haveBang) {//正常来说自己选择用不用，暂时让他有得用就用，自己可选择
            if (location != 0) { //不是玩家自己
                handCardList.remove(bangIdx); //从手牌中移除bang
                handCardList = sortHandCardList(handCardList);
                return false;
            } else {//是玩家
                //这时候希望出现一个按钮使不使用bang
                AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                dialog.setTitle("你要使用砰吗？");
                dialog.setCancelable(false);
                dialog.setPositiveButton("使用", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handCardList.remove(bangIdx2); //从手牌中移除bang
                        handCardList = sortHandCardList(handCardList);
                        CardAdapter adapter = new CardAdapter(handCardList);
                        mRecyclerView.setAdapter(adapter);
                        chooseYes = true;
                    }
                });
                dialog.setPositiveButton("不使用", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 扣血，更新显示
                        life--;
                        setTextView_life();
                    }
                });
                if(chooseYes) return false;
                else return true;
            }
        }else{//没有或者不使用bang
            // 扣血，更新显示
            life--;
            setTextView_life();
            return true;
        }
    }



    //恐慌   暂时设置成和窃贼一样 6
    public void panic(Normal normal){
        if(normal.handCardList.size() > 0){
            Random random = new Random();
            normal.handCardList.remove(random.nextInt(handCardList.size()));
            handCardList = normal.sortHandCardList(handCardList);
        }
        for(int i = 0;i<handCardList.size();i++){
            if(handCardList.get(i).getKey()==6){
                handCardList.remove(i);
                handCardList = sortHandCardList(handCardList);
                break;
            }
        }
    }

    //印地安人入侵 (暂不实现) 7
    public void indians(){
        for(int i = 0;i<handCardList.size();i++){
            if(handCardList.get(i).getKey()==7){
                handCardList.remove(i);
                handCardList = sortHandCardList(handCardList);
                break;
            }
        }
    }


    //加特林 暂不实现 9
    public void gatling(){
        for(int i = 0;i<handCardList.size();i++){
            if(handCardList.get(i).getKey()==9){
                handCardList.remove(i);
                handCardList = sortHandCardList(handCardList);
                break;
            }
        }
    }

    //小马车 10
    public void stagecoach(){
        //抽两张牌 Main写
    }

    //大马车 11
    public void wellsFargo(){
        //抽三张牌 Main写
    }



    //沙龙  12
    public void saloon(){
        for(int i = 0;i<handCardList.size();i++){
            if(handCardList.get(i).getKey()==12){
                handCardList.remove(i);
                handCardList = sortHandCardList(handCardList);
                break;
            }
        }
    }



    //枪类装备移除方法 13 14 15 16 17 当原本更新枪的装备时用
    void deleteEquip(){
        for(int i = 0;i<equipList.size();i++){
            if(equipList.get(i).getKey()==13 ||
                    equipList.get(i).getKey()==14 ||
                    equipList.get(i).getKey()==15 ||
                    equipList.get(i).getKey()==16 ||
                    equipList.get(i).getKey()==17 ){
                equipList.remove(i);
                haveShotgun = false;
                break;
            }
        }
    }

    //霰弹枪 13
    public void shotgun(){
        //装备到装备区
        attackDistance = 1;
        canUseMoreBang = true;
        deleteEquip();
        for(int i = 0;i<handCardList.size();i++){
            if(handCardList.get(i).getKey()==13){
                equipList.add(handCardList.get(i));
                handCardList.remove(i);
                handCardList = sortHandCardList(handCardList);
                break;
            }
        }
    }

    //步枪 14
    public void rifle(){
        //装备到装备区
        attackDistance = 2;
        canUseMoreBang = false;
        deleteEquip();
        for(int i = 0;i<handCardList.size();i++){
            if(handCardList.get(i).getKey()==14){
                equipList.add(handCardList.get(i));
                handCardList.remove(i);
                handCardList = sortHandCardList(handCardList);
                break;
            }
        }
    }

    //左轮枪 15
    public void revolver(){
        //装备到装备区
        attackDistance = 3;
        canUseMoreBang = false;
        deleteEquip();
        for(int i = 0;i<handCardList.size();i++){
            if(handCardList.get(i).getKey()==15){
                equipList.add(handCardList.get(i));
                handCardList.remove(i);
                handCardList = sortHandCardList(handCardList);
                break;
            }
        }
    }

    //卡宾枪 16
    public void carbine(){
        //装备到装备区
        attackDistance = 4;
        canUseMoreBang = false;
        deleteEquip();
        for(int i = 0;i<handCardList.size();i++){
            if(handCardList.get(i).getKey()==16){
                equipList.add(handCardList.get(i));
                handCardList.remove(i);
                handCardList = sortHandCardList(handCardList);
                break;
            }
        }
    }

    //狙击枪 17
    public void snipingRifle(){
        //装备到装备区
        attackDistance = 5;
        canUseMoreBang = false;
        deleteEquip();
        for(int i = 0;i<handCardList.size();i++){
            if(handCardList.get(i).getKey()==17){
                equipList.add(handCardList.get(i));
                handCardList.remove(i);
                handCardList = sortHandCardList(handCardList);
                break;
            }
        }
    }

    //酒桶 18
    public void barrel(){
        haveBarrel += 1;
        //装备到装备区
        for(int i = 0;i<handCardList.size();i++){
            if(handCardList.get(i).getKey()==18){
                equipList.add(handCardList.get(i));
                handCardList.remove(i);
                handCardList = sortHandCardList(handCardList);
                break;
            }
        }

    }

    //监狱 (暂不实现) 19
    public void jail(){
        //TODO
        for(int i = 0;i<handCardList.size();i++){
            if(handCardList.get(i).getKey()==19){
                handCardList.remove(i);
                handCardList = sortHandCardList(handCardList);
                break;
            }
        }
    }


    //炸药（暂不实现）20
    public void dynamite(){
        //TODO
        for(int i = 0;i<handCardList.size();i++){
            if(handCardList.get(i).getKey()==20){
                handCardList.remove(i);
                handCardList = sortHandCardList(handCardList);
                break;
            }
        }
    }

    //瞄准镜 21
    public void scope(){
        haveScope = true;
        for(int i = 0;i<handCardList.size();i++){
            if(handCardList.get(i).getKey()==21){
                equipList.add(handCardList.get(i));
                handCardList.remove(i);
                handCardList = sortHandCardList(handCardList);
                break;
            }
        }
    }

    //野马 22
    public void mustang(){
        haveMustang = true;
        for(int i = 0;i<handCardList.size();i++){
            if(handCardList.get(i).getKey()==22){
                equipList.add(handCardList.get(i));
                handCardList.remove(i);
                handCardList = sortHandCardList(handCardList);
                break;
            }
        }
    }


}
