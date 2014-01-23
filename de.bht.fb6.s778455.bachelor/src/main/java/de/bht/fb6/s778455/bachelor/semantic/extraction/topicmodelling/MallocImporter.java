package de.bht.fb6.s778455.bachelor.semantic.extraction.topicmodelling;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.FeatureSequence2FeatureVector;
import cc.mallet.pipe.Input2CharSequence;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.PrintInputAndTarget;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.Target2Label;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceLowercase;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;

public class MallocImporter {
    Pipe pipe;
    
    public MallocImporter(final File tempDataFile, final File stoplistFile, final String encoding) {
        this.pipe = this._buildPipe(stoplistFile, encoding);
        this.readFile(tempDataFile);
    }

    private void readFile( final File tempDataFile ) {
        
        
    }

    private Pipe _buildPipe(final File stoplistFile, final String encoding) {
        final ArrayList< Pipe > pipeList = new ArrayList<>();
        
        pipeList.add( new Input2CharSequence("UTF-8") );
        
        final Pattern tokenPattern =
                Pattern.compile("[\\p{L}\\p{N}_]+");
        
        pipeList.add( new CharSequence2TokenSequence( tokenPattern ) );
        
        pipeList.add( new TokenSequenceLowercase() );
        
        pipeList.add( new TokenSequenceRemoveStopwords( stoplistFile, encoding, false, true, false ) );
        
        pipeList.add( new TokenSequence2FeatureSequence() );
        
        pipeList.add(new Target2Label());
        
        pipeList.add(new FeatureSequence2FeatureVector());
        
        pipeList.add(new PrintInputAndTarget());
        
        return new SerialPipes( pipeList );
    }

}
