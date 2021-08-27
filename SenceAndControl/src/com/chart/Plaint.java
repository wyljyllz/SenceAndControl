package com.chart;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
/*author:���˵���ͽ
 * date:2019.7.18
 * arm:java��̬��ͼ�ķ���
 */
public class Plaint extends JPanel {
	private List<Integer> values;                      // �洢���ݵļ��ϣ��̲߳���ȫ
	private static final int MAX_VALUE = 200;          // ���ֵ�����������y���ݣ�
	private static final int MAX_COUNT_OF_VALUES = 50; // ���ϵ��������
	public static JFrame frame = new JFrame("java��̬����ͼ��");
	public Plaint() {
		//һ���̰߳�ȫ�ļ���
		values = Collections.synchronizedList(new ArrayList<Integer>());
		// ʹ���̵߳�Ŀ�ģ���̬�ģ�����������������Ӻ�ȥ������
		new Thread(new Runnable() {
			public void run() {
				//һ���������׼ȷ��˵��С��200��
				Random rand = new Random();
				try {
					while (true) {
						//���ɵ�����Ӽ��ϣ����������������ݶ��˾��޳�����
						addValue(rand.nextInt(MAX_VALUE)); // ����һ�����ݣ���ģ����ղ��ŵ�������.
						//System.out.println("�߳�");
						//�����һ���ػ�ķ��������̰߳�ȫ�ģ����Ե���paintComponent(Graphics g) ����
						repaint();
						Thread.sleep(1000);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//׼������
		Graphics2D g2d = (Graphics2D) g;
		//���û�������������
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//���ÿ��
		int w = getWidth();
		int h = getHeight();
		//����x�᷽��ĵ�λ����;�����400px��������������������50;��ôһ�����ݾ�ռ��8px
		int xDelta = w / MAX_COUNT_OF_VALUES;
		//����Ǽ��ϵ�ʵʱ���ȣ��϶���С�ڻ����MAX_COUNT_OF_VALUES
		int length = values.size();
		//�Լ�����������ݽ��л��ƣ���һ����͵ڶ�����Ҫ����
		for (int i = 0; i < length - 1; ++i) {
			/*
			 * x���λ�ã�xDelta * (MAX_COUNT_OF_VALUES - length + i)  ������ϵ�����û���������һ�������һ�������ұߣ���һ���Ǽ��ϵ���󳤶�-���ϵ�ǰ����+1��
			 * y���λ�ã�normalizeValueForYAxis(values.get(i), h)
			 * ǰһ�����ݺͺ�һ������������
			 */
			g2d.drawLine(xDelta * (MAX_COUNT_OF_VALUES - length + i), normalizeValueForYAxis(values.get(i), h),
					xDelta * (MAX_COUNT_OF_VALUES - length + i + 1), normalizeValueForYAxis(values.get(i + 1), h));
		}
	}

	/**
	 * ���յ������ݷ����ڴ�.
	 * @param value
	 */
	private void addValue(int value) {
		// ѭ����ʹ��һ���������ݵĿռ�.
		// �����ʵ��һ��ѭ�����飬������͵����ʹ��ArrayList.
		if (values.size() > MAX_COUNT_OF_VALUES) {
			values.remove(0);
		}

		values.add(value);
	}

	/**
	 * ��һ��y�᷽���ֵ. ʹ��value��y���ֵΪ[0, height]֮��.
	 */
	private int normalizeValueForYAxis(int value, int height) {
		return (int) ((double) height / MAX_VALUE * value);
	}

	public static void createGuiAndShow() {
		//���������
		//JFrame frame = new JFrame("java��̬����ͼ��");
		//����
		frame.getContentPane().add(new Plaint());
		//���ùرմ�����ǹر���������
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//���ô���Ĵ�С
		frame.setSize(500, 300);
		frame.setLocationRelativeTo(null);
		//���ô���ɼ�
		frame.setVisible(true);
	}


}