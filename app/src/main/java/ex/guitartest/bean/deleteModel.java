package ex.guitartest.bean;

import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;

import ex.guitartest.teacher.SpectrumWriteActivity;
import ex.guitartest.util.SizeSwitch;
import ex.guitartest.viewutils.MusicNoteLayout;

/**
 *
 * Created by qyxlx on 2018/3/12.
 */
public class deleteModel {

	public void setMusicNoteLayout(MusicNoteLayout musicNoteLayout,
                                    RelativeLayout inputLayout,
                                   SpectrumWriteActivity activity) {
		int indexCursorLine = musicNoteLayout.getIndexCursorLine();
		int indexCursorRow = musicNoteLayout.getIndexCursorRow();
		int line = musicNoteLayout.getLine();
		float size = musicNoteLayout.getSize();

		if (!(indexCursorLine == 0 && indexCursorRow == 0)) {

			ArrayList<Integer> standardNums = musicNoteLayout.getStandardNums();

			standardNums.remove(line * indexCursorRow + indexCursorLine - 1);
			musicNoteLayout.setStandardNums(standardNums);
			if (indexCursorLine == 0) {
				musicNoteLayout.setIndexCursorLine(line - 1);
				musicNoteLayout.setIndexCursorRow(indexCursorRow - 1);
				musicNoteLayout.setX((line - 1) * 26 * size
						+ SizeSwitch.dip2px(activity, 1));
				musicNoteLayout.setY(musicNoteLayout.getY() - 31 * size);
			} else {
				musicNoteLayout.setIndexCursorLine(indexCursorLine - 1);
				musicNoteLayout.setX(musicNoteLayout.getX() - 26 * size);
			}
			musicNoteLayout.drawCursor();

			inputLayout.removeView(inputLayout.findViewById((line
					* indexCursorRow + indexCursorLine - 1) * 6 + 1));
			inputLayout.removeView(inputLayout.findViewById((line
					* indexCursorRow + indexCursorLine - 1) * 6 + 2));
			inputLayout.removeView(inputLayout.findViewById((line
					* indexCursorRow + indexCursorLine - 1) * 6 + 3));
			inputLayout.removeView(inputLayout.findViewById((line
					* indexCursorRow + indexCursorLine - 1) * 6 + 4));
			inputLayout.removeView(inputLayout.findViewById((line
					* indexCursorRow + indexCursorLine - 1) * 6 + 5));
			inputLayout.removeView(inputLayout.findViewById((line
					* indexCursorRow + indexCursorLine - 1) * 6 + 6));


			TextView textView;
			for (int i = line * indexCursorRow + indexCursorLine - 1; i < standardNums
					.size(); i++) {
				for (int j = 1; j < 7; j++) {
					textView = inputLayout.findViewById((i + 1) * 6
							+ j);
					if (textView.getX() <= 26 * size) {
						textView.setX((line - 1) * 26 * size + textView.getX());
						textView.setY(textView.getY() - 31 * size);
					} else {
						textView.setX(textView.getX() - 26 * size);
					}
					textView.setId(i * 6 + j);
				}
			}

		}
	}

}
