package android.extra.emsh;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.text.TextUtils;

import android.extra.emsh.EmshConstant;
import static android.extra.emsh.EmshConstant.EmshBatteryStatus;


public class EmshUtils {

	public static boolean EmshBatteryStatus_isEqual( final EmshBatteryStatus a, final EmshBatteryStatus b )
	{
		if ( a == null || b == null )
			return false;

		if ( a.SessionStatus != b.SessionStatus )
			return false;

		if ( a.HardwareStatus != b.HardwareStatus )
			return false;
		
		if ( a.ProtocolVersion != b.ProtocolVersion )
			return false;

		if ( (a.DeviceModelNumber == null && b.DeviceModelNumber != null) ||
			 (a.DeviceModelNumber != null && b.DeviceModelNumber == null) ||
			 (!a.DeviceModelNumber.equals(b.DeviceModelNumber)) )
			return false;

		if ( a.BatteryPowerMode != b.BatteryPowerMode )
			return false;

		if ( a.BatteryCapacityLevel != b.BatteryCapacityLevel )
			return false;

		if ( a.BatteryTemperatureStatus != b.BatteryTemperatureStatus )
			return false;

		if ( a.BatteryTerminalVoltage != b.BatteryTerminalVoltage )
			return false;

		if ( a.BatteryDischargeVoltage != b.BatteryDischargeVoltage )
			return false;

		if ( a.BatteryDischargeCurrent != b.BatteryDischargeCurrent )
			return false;

		return true;
	}

	public static void EmshBatteryStatus_Clear( EmshBatteryStatus v )
	{
		if ( v != null ) {
			v.SessionStatus 			= 0;
			v.HardwareStatus			= 0;
			v.ProtocolVersion			= 0;
			v.DeviceModelNumber			= null;
			v.BatteryPowerMode			= 0;
			v.BatteryCapacityLevel		= 0;
			v.BatteryCapacityPercent	= 0;
			v.BatteryTemperatureStatus	= 0;
			v.BatteryTerminalVoltage	= 0;
			v.BatteryDischargeVoltage	= 0;
			v.BatteryDischargeCurrent	= 0;
			v.LatestUpdateUnixTime		= 0;
		}
	}

	public static void EmshBatteryStatus_Duplicate( EmshBatteryStatus dst, final EmshBatteryStatus src )
	{
		if ( dst != null && src != null ) {
			dst.SessionStatus = src.SessionStatus;
			dst.HardwareStatus = src.HardwareStatus;
			dst.ProtocolVersion = src.ProtocolVersion;
			dst.DeviceModelNumber = src.DeviceModelNumber;
			dst.BatteryPowerMode = src.BatteryPowerMode;
			dst.BatteryCapacityLevel = src.BatteryCapacityLevel;
			dst.BatteryCapacityPercent = src.BatteryCapacityPercent;
			dst.BatteryTemperatureStatus = src.BatteryTemperatureStatus;
			dst.BatteryTerminalVoltage = src.BatteryTerminalVoltage;
			dst.BatteryDischargeVoltage = src.BatteryDischargeVoltage;
			dst.BatteryDischargeCurrent = src.BatteryDischargeCurrent;
			dst.LatestUpdateUnixTime = src.LatestUpdateUnixTime;
		}
	}

	public static String EmshBatteryStatus_toString( final EmshBatteryStatus v )
	{
		if ( v != null )
		{
			StringBuilder sBuilder = new StringBuilder();
			sBuilder.append("SessionStatus = 0x" + Integer.toHexString(v.SessionStatus) + ", " );
			sBuilder.append("HardwareStatus = " + Integer.toString(v.HardwareStatus) + ", " );
			sBuilder.append("ProtocolVersion = " + Integer.toString(v.ProtocolVersion) + ", " );
			sBuilder.append("DeviceModelNumber = " + (TextUtils.isEmpty(v.DeviceModelNumber) ? "(null)" : v.DeviceModelNumber) + ", " );
			sBuilder.append("BatteryPowerMode = " + EmshConstant.getPowerModeDesc(v) + ", " );
			sBuilder.append("BatteryCapacityLevel = " + Integer.toString(v.BatteryCapacityLevel) + ", " );
			sBuilder.append("BatteryTemperatureStatus = 0x" + Integer.toHexString(v.BatteryTemperatureStatus) + ", " );
			sBuilder.append("BatteryTerminalVoltage = " + Integer.toString(v.BatteryTerminalVoltage) + "mV, " );
			sBuilder.append("BatteryDischargeVoltage = " + Integer.toString(v.BatteryDischargeVoltage) + "mV, ");
			sBuilder.append("BatteryDischargeCurrent = " + Integer.toString(v.BatteryDischargeCurrent) + "mA, ");
	
			SimpleDateFormat fmtDate = new SimpleDateFormat("HH:mm:ss", Locale.PRC);
			Date dtLastUpdate = new Date( v.LatestUpdateUnixTime * 1000 );
			sBuilder.append("tmLatestUpdate = " + fmtDate.format(dtLastUpdate) );
			sBuilder.append(System.getProperty("line.separator"));
	
			return sBuilder.toString();
		}

		return null;
	}
}
