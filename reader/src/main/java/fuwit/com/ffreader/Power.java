package fuwit.com.ffreader;

import java.io.IOException;


/**
 * ��Դ.<br>
 * <br>
 * CreateDate: 2013-9-27<br>
 * Copyright: Copyright(c) 2013-9-27<br>
 * <br>
 * 
 * @since v1.0.0
 * @Description 2013-9-27::::��������</br>
 */
public class Power {
	/**
	 * ��Դ����.<br>
	 * <br>
	 * 
	 * @Description 2013-9-27::::�����˷���</br>
	 */
	public static void on() {
		try {
			CommunicateShell.postShellComm("echo off >/sys/class/gpio_switch/gpio_fucn4");// 3v��Դ

			CommunicateShell.postShellComm("echo on >/sys/class/gpio_switch/vbat_en");// 3v��Դ

			CommunicateShell.postShellComm("echo on >/sys/class/gpio_switch/usb_dc_en");// 3v��Դ

			CommunicateShell.postShellComm("echo on >/sys/class/gpio_switch/gpio_fucn2");// 16��
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void on(long waitTime) {
		try {
			CommunicateShell.postShellComm("echo off >/sys/class/gpio_switch/gpio_fucn4");// 3v��Դ
			try {
				Thread.sleep(waitTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			CommunicateShell.postShellComm("echo on >/sys/class/gpio_switch/vbat_en");// 3v��Դ
			try {
				Thread.sleep(waitTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			CommunicateShell.postShellComm("echo on >/sys/class/gpio_switch/usb_dc_en");// 3v��Դ
			try {
				Thread.sleep(waitTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			CommunicateShell.postShellComm("echo on >/sys/class/gpio_switch/gpio_fucn2");// 16��
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��Դ�ر�.<br>
	 * <br>
	 * 
	 * @Description 2013-9-27::::�����˷���</br>
	 */
	public static void off() {
		try {
			CommunicateShell.postShellComm("echo off >/sys/class/gpio_switch/gpio_fucn2");// 16��

			CommunicateShell.postShellComm("echo off >/sys/class/gpio_switch/usb_dc_en");// 3v��Դ

			CommunicateShell.postShellComm("echo off >/sys/class/gpio_switch/vbat_en");// 3v��Դ
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public static void off(long waitTime) {
		try {
			CommunicateShell.postShellComm("echo off >/sys/class/gpio_switch/gpio_fucn2");// 16��
			try {
				Thread.sleep(waitTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			CommunicateShell.postShellComm("echo off >/sys/class/gpio_switch/usb_dc_en");// 3v��Դ
			try {
				Thread.sleep(waitTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			CommunicateShell.postShellComm("echo off >/sys/class/gpio_switch/vbat_en");// 3v��Դ
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
