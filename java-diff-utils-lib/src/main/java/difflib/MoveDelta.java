package difflib;

import difflib.Chunk;
import difflib.Delta;
import difflib.PatchFailedException;
import difflib.Delta.TYPE;
import java.util.List;

public class MoveDelta<T> extends Delta<T> {
    public MoveDelta(Chunk<T> original, Chunk<T> revised) {
        super(original, revised);
    }

    public void applyTo(List<T> target) throws PatchFailedException {
        this.verify(target);
        int position = this.getOriginal().getPosition();
        List<T> lines = this.getRevised().getLines();

        for(int i = 0; i < lines.size(); ++i) {
            target.add(position + i, lines.get(i));
        }

    }

    public void restore(List<T> target) {
        int position = this.getRevised().getPosition();
        int size = this.getRevised().size();

        for(int i = 0; i < size; ++i) {
            target.remove(position);
        }

    }

    public void verify(List<T> target) throws PatchFailedException {
        if(this.getOriginal().getPosition() > target.size()) {
            throw new PatchFailedException("Incorrect patch for delta: delta original position > target size");
        }
    }

    public TYPE getType() {
        return TYPE.MOVE;
    }

    public String toString() {
        return "[MoveDelta, position: " + this.getOriginal().getPosition() + ", lines: " + this.getRevised().getLines() + "]";
    }
}
