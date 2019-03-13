package com.xuyun.platform.dsfcenterdata.enums;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PlatformType {

  Sport("体育博彩","sport"),
  VideoGame("真人视讯","vodeoGame"),
  ComputerGame("电子游戏","computerGame"),
  Fish("捕鱼","fish"),
  Lottery("彩票","lottery"),
  Poker("棋牌","poker");

  public String title;
  public String code;

  public static List<PlatformType> aginPlatformTypes(){
    List<PlatformType> types = new ArrayList<>();
    types.add(PlatformType.Sport);
    types.add(PlatformType.VideoGame);
    types.add(PlatformType.ComputerGame);
    types.add(PlatformType.Fish);
    return types ;
  }

  public static List<PlatformType> dsPlatformTypes(){
    List<PlatformType> types = new ArrayList<>();
    types.add(PlatformType.VideoGame);
    return types ;
  }

  public static List<PlatformType> fgPlatformTypes(){
    List<PlatformType> types = new ArrayList<>();
    types.add(PlatformType.ComputerGame);
    types.add(PlatformType.Fish);
    types.add(PlatformType.Poker);
    return types ;
  }

  public static List<PlatformType> kyPlatformTypes(){
    List<PlatformType> types = new ArrayList<>();
    types.add(PlatformType.Poker);
    return types ;
  }
}
