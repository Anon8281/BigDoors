package nl.pim16aap2.bigDoors.util;

import java.util.HashMap;
import java.util.Map;

public enum DoorType
{
    DOOR        (0, "-BD", "GENERAL.DOORTYPE.Door", "bigdoors.user.createdoor.door",
                 DoorAttribute.LOCK, DoorAttribute.TOGGLE, DoorAttribute.INFO, DoorAttribute.DELETE,
                 DoorAttribute.RELOCATEPOWERBLOCK, DoorAttribute.CHANGETIMER, DoorAttribute.ADDOWNER,
                 DoorAttribute.REMOVEOWNER, DoorAttribute.DIRECTION_ROTATE, DoorAttribute.NOTIFICATIONS,
                 DoorAttribute.BYPASS_PROTECTIONS),


    DRAWBRIDGE  (1, "-DB", "GENERAL.DOORTYPE.DrawBridge", "bigdoors.user.createdoor.drawbridge",
                 DoorAttribute.LOCK, DoorAttribute.TOGGLE, DoorAttribute.INFO, DoorAttribute.DELETE,
                 DoorAttribute.RELOCATEPOWERBLOCK, DoorAttribute.CHANGETIMER, DoorAttribute.ADDOWNER,
                 DoorAttribute.REMOVEOWNER, DoorAttribute.DIRECTION_STRAIGHT, DoorAttribute.NOTIFICATIONS,
                 DoorAttribute.BYPASS_PROTECTIONS),


    PORTCULLIS  (2, "-PC", "GENERAL.DOORTYPE.Portcullis", "bigdoors.user.createdoor.portcullis",
                 DoorAttribute.LOCK, DoorAttribute.TOGGLE, DoorAttribute.INFO, DoorAttribute.DELETE,
                 DoorAttribute.RELOCATEPOWERBLOCK, DoorAttribute.CHANGETIMER, DoorAttribute.ADDOWNER,
                 DoorAttribute.REMOVEOWNER, DoorAttribute.DIRECTION_STRAIGHT, DoorAttribute.BLOCKSTOMOVE,
                 DoorAttribute.NOTIFICATIONS, DoorAttribute.BYPASS_PROTECTIONS),


    SLIDINGDOOR (3, "-SD", "GENERAL.DOORTYPE.SlidingDoor", "bigdoors.user.createdoor.slidingdoor",
                 DoorAttribute.LOCK, DoorAttribute.TOGGLE, DoorAttribute.INFO, DoorAttribute.DELETE,
                 DoorAttribute.RELOCATEPOWERBLOCK, DoorAttribute.CHANGETIMER, DoorAttribute.ADDOWNER,
                 DoorAttribute.REMOVEOWNER, DoorAttribute.DIRECTION_STRAIGHT, DoorAttribute.BLOCKSTOMOVE,
                 DoorAttribute.NOTIFICATIONS, DoorAttribute.BYPASS_PROTECTIONS),
    ;

    private int    val;
    private String flag;
    private String nameKey;
    private String permission;
    private String friendlyName;
    private static Map<Integer, DoorType> valMap  = new HashMap<>();
    private static Map<String,  DoorType> flagMap = new HashMap<>();
    private DoorAttribute[] attributes;

    private DoorType(int val, String flag, String nameKey, String permission, DoorAttribute... attributes)
    {
        this.val  = val;
        this.flag = flag;
        this.nameKey = nameKey;
        this.permission = permission;
        this.attributes = attributes;
        friendlyName = this.name().substring(0, 1) + this.name().substring(1).toLowerCase();
    }

    public static int             getValue       (DoorType type) {  return type.val;           }
    public static String          getNameKey     (DoorType type) {  return type.nameKey;       }
    public static String          getPermission  (DoorType type) {  return type.permission;    }
    public static DoorType        valueOf        (int type)      {  return valMap.get(type);   }
    public static DoorType        valueOfFlag    (String flag)   {  return flagMap.get(flag);  }
    public static DoorAttribute[] getAttributes  (DoorType type) {  return type.attributes;    }
    public static String          getFriendlyName(DoorType type) {  return type.friendlyName;  }

    static
    {
        for (DoorType type : DoorType.values())
        {
            valMap.put( type.val,  type);
            flagMap.put(type.flag, type);
        }
    }
}
