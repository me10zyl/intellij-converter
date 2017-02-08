package com.github.me10zyl;

import com.github.me10zyl.bussiness.Converter;
import com.github.me10zyl.entity.ClassProperty;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZyL on 2017/1/22.
 */
public class GUI extends JPanel{
    private JPanel panel1;
    private JButton OKButton;
    private JPanel lpanel;
    private JPanel rpanel;
    private JTextArea textArea1;
    private JLabel label1;
    private JLabel label2;
    private JCheckBox last;
    private JCheckBox secondLast;
    private List<ClassProperty> props1, props2;

    class OnChecked implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            JCheckBox checkBox = (JCheckBox) e.getSource();
            MyPanel m = (MyPanel) checkBox.getParent();
            int index = m.getIndex();
            MyPanel l = (MyPanel) lpanel.getComponent(index);
            MyPanel r = (MyPanel) rpanel.getComponent(index);
            JCheckBox c1 = (JCheckBox) l.getComponent(0);
            JCheckBox c2 = (JCheckBox) r.getComponent(0);
            c1.setSelected(checkBox.isSelected());
            c2.setSelected(checkBox.isSelected());
            props1.get(index).setEnable(checkBox.isSelected());
            props2.get(index).setEnable(checkBox.isSelected());
        }
    }

    public GUI() {
        props1 = new ArrayList<ClassProperty>();
        props2 = new ArrayList<ClassProperty>();
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Converter converter = new Converter(props1, props2);
                String res = converter.generate();
                textArea1.setText(res);
            }
        });
        this.setLayout(new BorderLayout());
        this.add(panel1);
    }

    public void updateData(){
      /*  for(int i = 0; i < 4 ;i ++){
            props1.add(new ClassProperty("prop"+i));
        }
        for(int i = 0; i < 4 ;i ++){
            props2.add(new ClassProperty("prop"+i));
        }*/
        if(props1.size() > 0){
            label1.setText(props1.get(0).getPsiClass().getQualifiedName());
        }
        if(props2.size() > 0){
            label2.setText(props2.get(0).getPsiClass().getQualifiedName());
        }
        createRow(lpanel, props1);
        createRow(rpanel, props2);
    }

    private int indexOf(Container parent, MyPanel panel){
        for(int i = 0; i< parent.getComponentCount(); i++){
            MyPanel component = (MyPanel) parent.getComponent(i);
            if(component == panel){
                return i;
            }
        }
        return -1;
    }

    private void createRow(JPanel panel, List<ClassProperty> props){
        int i = 0;
        panel.removeAll();
        for(ClassProperty prop : props){
            JCheckBox check1 = new JCheckBox(prop.getName());
            check1.setSelected(prop.isEnable());
            check1.addActionListener(new OnChecked());
            MyPanel j = new MyPanel();
            j.setIndex(i++);
            FlowLayout f = (FlowLayout) j.getLayout();
            f.setAlignment(FlowLayout.LEFT);
            j.add(check1);
            JButton up = new JButton("↑");
            JButton down = new JButton("↓");
            j.add(up);
            j.add(down);
            if(prop.isPlaceHolder()){
                check1.setForeground(Color.RED);
            }
            panel.add(j);

            up.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton b = (JButton) e.getSource();
                    MyPanel row = (MyPanel) b.getParent();

                    int index = indexOf(row.getParent(), row);
                    int current = index - 1;
                    if(current < 0){
                        current = row.getParent().getComponentCount() - 1;
                    }

                    props.remove(index);
                    props.add(current, prop);

                    panel.add(row, current);
                    panel.updateUI();
                }
            });

            down.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JButton b = (JButton) e.getSource();
                    MyPanel row = (MyPanel) b.getParent();
                    int index = indexOf(row.getParent(), row);
                    int current = index + 1;
                    if(current > row.getParent().getComponentCount() - 1){
                         current = 0;
                    }
                    props.remove(index);
                    props.add(current, prop);

                    panel.add(row, current);
                    panel.updateUI();
                }
            });
        }
        panel.updateUI();
    }

    public JPanel getPanel1() {
        return panel1;
    }

    public void drawLine(int x1,int y1,int x2,int y2){
        ((MyPanel)lpanel).updateGraphics(x1, y1, x2, y2);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        lpanel = new MyPanel();
        lpanel.setLayout(new BoxLayout(lpanel, BoxLayout.Y_AXIS));
        rpanel = new MyPanel();
        rpanel.setLayout(new BoxLayout(rpanel, BoxLayout.Y_AXIS));
    }

    class MyPanel extends JPanel{

        private int index;


        private int x1, y1, x2, y2;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        @Override
        protected void printComponent(Graphics g) {
            super.printComponent(g);
            if(!(x1 == 0 && y1 == 0 && x2 == 0 && y2 == 0)){
                g.drawLine(x1, y1, x2, y2);
            }
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            if(!(x1 == 0 && y1 == 0 && x2 == 0 && y2 == 0)){
                g.drawLine(x1, y1, x2, y2);
            }
        }

        public void updateGraphics(int x1, int y1, int x2, int y2){
            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
            this.y2 = y2;
            repaint();
        }
    }

    public List<ClassProperty> getProps1() {
        return props1;
    }


    public List<ClassProperty> getProps2() {
        return props2;
    }
}
