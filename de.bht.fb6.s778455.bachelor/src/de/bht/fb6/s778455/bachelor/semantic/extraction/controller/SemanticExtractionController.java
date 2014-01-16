/**
 * Copyright (c) 2013-2014 Sascha Feldmann (sascha.feldmann@gmx.de) 
 */
package de.bht.fb6.s778455.bachelor.semantic.extraction.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.bht.fb6.s778455.bachelor.model.AUserContribution;
import de.bht.fb6.s778455.bachelor.model.Board;
import de.bht.fb6.s778455.bachelor.model.BoardThread;
import de.bht.fb6.s778455.bachelor.model.Course;
import de.bht.fb6.s778455.bachelor.model.Posting;
import de.bht.fb6.s778455.bachelor.organization.Application;
import de.bht.fb6.s778455.bachelor.organization.Application.LogType;
import de.bht.fb6.s778455.bachelor.organization.GeneralLoggingException;
import de.bht.fb6.s778455.bachelor.organization.IConfigKeys;
import de.bht.fb6.s778455.bachelor.organization.InvalidConfigException;
import de.bht.fb6.s778455.bachelor.semantic.extraction.AExtractionStrategy;
import de.bht.fb6.s778455.bachelor.semantic.organization.service.ServiceFactory;
import de.bht.fb6.s778455.bachelor.statistics.AStatisticsBuilder;
import de.bht.fb6.s778455.bachelor.statistics.GeneralStatisticsBuilder;

/**
 * <p>
 * This class realizes the controller for all tasks issuing the semantic
 * extraction..
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 08.01.2014
 * 
 */
public class SemanticExtractionController {
    protected List< String > extractionChain;
    private boolean printInfo = false;
    private ArrayList< AExtractionStrategy > germanNerStrategies;
    private ArrayList< AExtractionStrategy > englishNerStrategies;
    private ArrayList< AExtractionStrategy > germanPosStrategies;
    private ArrayList< AExtractionStrategy > englishPosStrategies;
    private AExtractionStrategy topicZoomStrategy;

    /**
     * Create a new semantic extraction controller.
     * 
     * @throws InvalidConfigException
     * @throws IllegalArgumentException
     */
    public SemanticExtractionController() throws InvalidConfigException {
        this.extractionChain = ServiceFactory.getConfigReader()
                .fetchMultipleValues(
                        IConfigKeys.SEMANTICS_EXTRACTION_STRATEGY_CHAIN );

        this.initializeStrategies();
    }

    /**
     * Initialize configured {@link AExtractionStrategy}.
     * 
     * @throws InvalidConfigException
     */
    private void initializeStrategies() throws InvalidConfigException {
        for( final String strategy : this.extractionChain ) {
            // try to get class
            Object clazz = null;

            /*
             * ###################### # # ANerStrategy # ######################
             */
            if( strategy.equals( IConfigKeys.SEMANTICS_EXTRACTION_STRATEGY_NER ) ) {
                // get German cascade
                this.germanNerStrategies = new ArrayList< AExtractionStrategy >();
                final List< String > germanCascade = ServiceFactory
                        .getConfigReader()
                        .fetchMultipleValues(
                                IConfigKeys.SEMANTICS_EXTRACTION_STRATEGY_NER_GERMAN_CASCADE );

                for( final String cascadeFile : germanCascade ) {
                    clazz = ServiceFactory.getConfigReader()
                            .getConfiguredClass(
                                    strategy,
                                    new File( ServiceFactory.getConfigReader()
                                            .fetchValue( cascadeFile ) ) );
                    if( !( clazz instanceof AExtractionStrategy ) ) {
                        throw new InvalidConfigException(
                                this.getClass()
                                        + "performSemanticExtraction: the configuration value for the key "
                                        + strategy
                                        + " doesn't point to a class extending AExtractionStrategy. Please check the configuration.",
                                "Illegal configuration value. Please check the logs.",
                                null );
                    }

                    this.germanNerStrategies
                            .add( ( AExtractionStrategy ) clazz );

                    if( this.printInfo ) {
                        System.out.println( "Initialized " + clazz.getClass()
                                + "\n" );
                    }
                }

                // get German cascade
                this.englishNerStrategies = new ArrayList< AExtractionStrategy >();
                final List< String > englishCascade = ServiceFactory
                        .getConfigReader()
                        .fetchMultipleValues(
                                IConfigKeys.SEMANTICS_EXTRACTION_STRATEGY_NER_ENGLISH_CASCADE );

                for( final String cascadeFile : englishCascade ) {
                    clazz = ServiceFactory.getConfigReader()
                            .getConfiguredClass(
                                    strategy,
                                    new File( ServiceFactory.getConfigReader()
                                            .fetchValue( cascadeFile ) ) );
                    if( !( clazz instanceof AExtractionStrategy ) ) {
                        throw new InvalidConfigException(
                                this.getClass()
                                        + "performSemanticExtraction: the configuration value for the key "
                                        + strategy
                                        + " doesn't point to a class extending AExtractionStrategy. Please check the configuration.",
                                "Illegal configuration value. Please check the logs.",
                                null );
                    }

                    this.englishNerStrategies
                            .add( ( AExtractionStrategy ) clazz );

                    if( this.printInfo ) {
                        System.out.println( "Initialized " + clazz.getClass()
                                + "\n" );
                    }
                }
            }

            /*
             * ###################### # # APosStrategy # ######################
             */
            if( strategy.equals( IConfigKeys.SEMANTICS_EXTRACTION_STRATEGY_POS ) ) {
                // get German cascade
                this.germanPosStrategies = new ArrayList< AExtractionStrategy >();
                final List< String > germanCascade = ServiceFactory
                        .getConfigReader()
                        .fetchMultipleValues(
                                IConfigKeys.SEMANTICS_EXTRACTION_STRATEGY_POS_GERMAN_CASCADE );

                for( final String cascadeFile : germanCascade ) {
                    clazz = ServiceFactory.getConfigReader()
                            .getConfiguredClass(
                                    strategy,
                                    new File( ServiceFactory.getConfigReader()
                                            .fetchValue( cascadeFile ) ) );
                    if( !( clazz instanceof AExtractionStrategy ) ) {
                        throw new InvalidConfigException(
                                this.getClass()
                                        + "performSemanticExtraction: the configuration value for the key "
                                        + strategy
                                        + " doesn't point to a class extending AExtractionStrategy. Please check the configuration.",
                                "Illegal configuration value. Please check the logs.",
                                null );
                    }

                    this.germanPosStrategies
                            .add( ( AExtractionStrategy ) clazz );

                    if( this.printInfo ) {
                        System.out.println( "Initialized " + clazz.getClass()
                                + "\n" );
                    }
                }

                // get German cascade
                this.englishPosStrategies = new ArrayList< AExtractionStrategy >();
                final List< String > englishCascade = ServiceFactory
                        .getConfigReader()
                        .fetchMultipleValues(
                                IConfigKeys.SEMANTICS_EXTRACTION_STRATEGY_POS_ENGLISH_CASCADE );

                for( final String cascadeFile : englishCascade ) {
                    clazz = ServiceFactory.getConfigReader()
                            .getConfiguredClass(
                                    strategy,
                                    new File( ServiceFactory.getConfigReader()
                                            .fetchValue( cascadeFile ) ) );
                    if( !( clazz instanceof AExtractionStrategy ) ) {
                        throw new InvalidConfigException(
                                this.getClass()
                                        + "performSemanticExtraction: the configuration value for the key "
                                        + strategy
                                        + " doesn't point to a class extending AExtractionStrategy. Please check the configuration.",
                                "Illegal configuration value. Please check the logs.",
                                null );
                    }

                    this.englishPosStrategies
                            .add( ( AExtractionStrategy ) clazz );

                    if( this.printInfo ) {
                        System.out.println( "Initialized " + clazz.getClass()
                                + "\n" );
                    }
                }
            }

            /*
             * ###################### # # TopicZoomStrategy #
             * ######################
             */

            if( strategy
                    .equals( IConfigKeys.SEMANTICS_EXTRACTION_STRATEGY_TOPICZOOM ) ) {
                clazz = ServiceFactory.getConfigReader().getConfiguredClass(
                        strategy );
                if( !( clazz instanceof AExtractionStrategy ) ) {
                    throw new InvalidConfigException(
                            this.getClass()
                                    + "performSemanticExtraction: the configuration value for the key "
                                    + strategy
                                    + " doesn't point to a class extending AExtractionStrategy. Please check the configuration.",
                            "Illegal configuration value. Please check the logs.",
                            null );
                }

                this.topicZoomStrategy = ( AExtractionStrategy ) clazz;

                if( this.printInfo ) {
                    System.out.println( "Initialized " + clazz.getClass()
                            + "\n" );
                }
            }
        }

    }

    /**
     * Perform the configured semantic extraction strategies.
     * 
     * @param inputCourses
     * @return
     * @throws InvalidConfigException
     */
    public Collection< Course > performSemanticExtraction(
            final Collection< Course > inputCourses )
            throws InvalidConfigException {
        if( null == inputCourses ) {
            throw new IllegalArgumentException(
                    "None of the given parameters may be null!" );
        }

        for( final Course course : inputCourses ) {
            // separate extraction between languages
            switch( course.getLanguage() ) {
            case ENGLISH:
                if( this.printInfo() ) {
                    System.out.println( "Enriching course " + course.getTitle()
                            + "with English strategies...\n" );
                }
                this.performExtraction( course, this.englishPosStrategies );
                this.performExtraction( course, this.englishNerStrategies );
                break;
            case GERMAN:
                if( this.printInfo() ) {
                    System.out.println( "Enriching course " + course.getTitle()
                            + "with German strategies...\n" );
                }
                this.performExtraction( course, this.germanPosStrategies );
                this.performExtraction( course, this.germanNerStrategies );
                this.performExtraction( course, this.topicZoomStrategy );
                break;
            default: // UNKNOWN
                Application
                        .log( this.getClass()
                                + ":performSemanticExtraction(): the given course ("
                                + course.getTitle()
                                + ")has an unknown language. I will not perform any extraction strategy.",
                                LogType.WARNING );
                break;
            }

            // go deeper into boards
            for( final Board board : course.getBoards() ) {
                switch( board.getLang() ) {
                case ENGLISH:
                    if( this.printInfo() ) {
                        System.out.println( "Enriching course "
                                + course.getTitle()
                                + "with English strategies...\n" );
                    }
                    this.performExtraction( board, this.englishPosStrategies );
                    this.performExtraction( board, this.englishNerStrategies );                   
                    break;
                case GERMAN:
                    if( this.printInfo() ) {
                        System.out.println( "Enriching course "
                                + course.getTitle()
                                + "with German strategies...\n" );
                    }
                    this.performExtraction( board, this.germanPosStrategies );
                    this.performExtraction( board, this.germanNerStrategies );
                    this.performExtraction( board, this.topicZoomStrategy );
                    break;
                default: // UNKNOWN
                    Application
                            .log( this.getClass()
                                    + ":performSemanticExtraction(): the given board ("
                                    + board.getTitle()
                                    + ")has an unknown language. I will not perform any extraction strategy.",
                                    LogType.WARNING );
                    break;
                }

                // go deeper into postings
                for( final BoardThread thread : board.getBoardThreads() ) {
                    for( final Posting posting : thread.getPostings() ) {
                        switch( posting.getLang() ) {
                        case ENGLISH:
                            if( this.printInfo() ) {
                                System.out.println( "Enriching course "
                                        + course.getTitle()
                                        + "with English strategies...\n" );
                            }
                            this.performExtraction( posting,
                                    this.englishPosStrategies );
                            this.performExtraction( posting,
                                    this.englishNerStrategies );
                            break;
                        case GERMAN:
                            if( this.printInfo() ) {
                                System.out.println( "Enriching course "
                                        + course.getTitle()
                                        + "with German strategies...\n" );
                            }
                            this.performExtraction( posting,
                                    this.germanPosStrategies );
                            this.performExtraction( posting,
                                    this.germanNerStrategies );
                            this.performExtraction( posting,
                                    this.topicZoomStrategy );
                            break;
                        default: // UNKNOWN
                            Application
                                    .log( this.getClass()
                                            + ":performSemanticExtraction(): the given posting ("
                                            + posting.getTitle()
                                            + ")has an unknown language. I will not perform any extraction strategy.",
                                            LogType.WARNING );
                            break;
                        }
                    }
                }
            }
        }

        // strategies only fill models attributes
        return inputCourses;
    }

    private void performExtraction( final Course course,
            final AExtractionStrategy strategy ) {
        if( this.printInfo ) {
            System.out.println( "Performing strategy "
                    + strategy.getClass() + " ..." );
        }
        
        try {
            strategy.extractSemantics( course );
        } catch( final GeneralLoggingException e ) {
            // continue, was logged already
        }
        
    }
    
    private void performExtraction( final AUserContribution contrib,
            final AExtractionStrategy strategy ) {
        if( this.printInfo ) {
            System.out.println( "Performing strategy "
                    + strategy.getClass() + " ..." );
        }
        
        try {
            strategy.extractSemantics( contrib );
        } catch( final GeneralLoggingException e ) {
            // continue, was logged already
        }
        
    }

    private void performExtraction( final AUserContribution contrib,
            final ArrayList< AExtractionStrategy > strategies ) {
        for( final AExtractionStrategy aExtractionStrategy : strategies ) {
            try {
                if( this.printInfo ) {
                    System.out.println( "Performing strategy "
                            + aExtractionStrategy.getClass() + " ..." );
                }
                aExtractionStrategy.extractSemantics( contrib );
            } catch( final GeneralLoggingException e ) {
                // continue, error is logged
            }
        }
    }

    private void performExtraction( final Course course,
            final ArrayList< AExtractionStrategy > strategies ) {
        for( final AExtractionStrategy aExtractionStrategy : strategies ) {
            try {
                if( this.printInfo ) {
                    System.out.println( "Performing strategy "
                            + aExtractionStrategy.getClass() + " ..." );
                }
                aExtractionStrategy.extractSemantics( course );
            } catch( final GeneralLoggingException e ) {
                // continue, error is logged
            }
        }
    }

    private boolean printInfo() {
        return this.printInfo;
    }

    public void setPrintInfo( final boolean printInfo ) {
        this.printInfo = printInfo;
    }

    /**
     * Get a statistics string.
     * 
     * @param anonymizedCourses
     * @param elapsedTime
     */
    public String getStatistics( final Collection< Course > anonymizedCourses ) {
        final StringBuilder statisticsBuilder = new StringBuilder();

        statisticsBuilder
                .append( "Used chain of extraction strategies (config keys): "
                        + this.extractionChain + "\n" );

        final AStatisticsBuilder builder = new GeneralStatisticsBuilder();
        statisticsBuilder.append( builder.buildStatistics( anonymizedCourses )
                .toString() + "\n" );

        return statisticsBuilder.toString();
    }
}
