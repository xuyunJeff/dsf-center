package com.xuyun.platform.dsfcenterdata.enums;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum  GamePlatform {

  /**
   * ag 枚举
   */
  Agin("Ag", PlatformType.aginPlatformTypes(),"Agin"),
  Ds("Ds", PlatformType.dsPlatformTypes(),"Ds"),
  Fg("Fg", PlatformType.fgPlatformTypes(),"FunGame"),
  Ky("Ky", PlatformType.kyPlatformTypes(),"Ky");

  public String platformCode;

  public List<PlatformType> platformType;

  public String name;

  public static GamePlatform getGamePlatform(String platformCode){
    for(GamePlatform gamePlatform : GamePlatform.values()){
      if(gamePlatform.platformCode.equals(platformCode)){
        return gamePlatform;
      }
    }
    return null;
  }
}
